package com.lihd.java;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: lihd-zookeeper
 * @description:
 * @author: li_hd
 * @create: 2020-04-18 17:19
 **/
public class Lock {


    //锁目录
    private static final String BASE_PATH = "/disLock";
    private static final String SPLIT_FLAG = "_";
    private Integer sessionOut;
    private String zkHost;
    private ZooKeeper zooKeeper;
    //当前锁节点名称
    private String lockNode;
    //等待锁节点名称
    private String waitNode;
    private CountDownLatch countDownLatch;

    public Lock(Integer sessionOut, String zkHost) {
        this.sessionOut = sessionOut;
        this.zkHost = zkHost;
    }

    //ZKDisLock初始化
    public void init() throws Exception {
        try {
            zooKeeper = new ZooKeeper(zkHost, sessionOut, (e) -> {});

            //判断有无根目录，没有的话创建
            Stat stat = zooKeeper.exists(BASE_PATH, false);
            if (stat == null) {
                zooKeeper.create(BASE_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new Exception("zk初始化失败", e);
        }
    }

    //加锁
    public void lock(String content) throws Exception {
        if (zooKeeper == null) {
            throw new Exception("zk未初始化");
        }
        try {
            //创建节点
            String itemName = zooKeeper.create(BASE_PATH + "/" + content + SPLIT_FLAG, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            //判断节点是否是目录中同一个content的最小节点
            if (isLowestNode(content, itemName)) {
                System.out.println("线程：" + Thread.currentThread().getName() + ",加锁成功,节点名称：" + lockNode);
            } else {
                System.out.println("线程：" + Thread.currentThread().getName() + ",等待锁：" + waitNode);
                //不是最小节点，等待锁释放
                waitForLock(sessionOut);

                //锁释放后，再次判断下是否是最小节点
                if (isLowestNode(content, itemName)) {
                    System.out.println("线程：" + Thread.currentThread().getName() + ",获得锁,节点名称：" + lockNode);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //判断是否是最小节点
    private boolean isLowestNode(String content, String itemName) throws Exception {
        //获取所有子节点
        List<String> nodeNames = zooKeeper.getChildren(BASE_PATH, null);
        List<String> itemNodes = new ArrayList<>();
        lockNode = itemName;
        //过滤content节点
        for (String name : nodeNames) {
            if (name.split(SPLIT_FLAG)[0].equals(content)) {
                itemNodes.add(name);
            }
        }
        //排序
        Collections.sort(itemNodes);

        //最小节点返回
        if (itemName.equals(BASE_PATH + "/" + itemNodes.get(0))) {
            return true;
        }
        //非最小节点，找到前一个节点
        int nodeIndex = Collections.binarySearch(itemNodes, itemName.substring(itemName.lastIndexOf("/") + 1));
        waitNode = itemNodes.get(nodeIndex - 1);
        return false;
    }

    //等待锁
    private void waitForLock(final Integer waitTime) throws Exception {

        //判断等待节点是否释放，同时注册watcher，通知释放事件
        Stat stat = zooKeeper.exists(BASE_PATH + "/" + waitNode, watchedEvent -> {
            System.out.println("watch process " + watchedEvent.toString());
            if (countDownLatch != null) {
                //锁释放，通知等待节点
                countDownLatch.countDown();
            }
        });
        //如果等待的锁不存在返回
        if (stat == null) {
            return;
        }
        //存在，等待
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await(waitTime, TimeUnit.MILLISECONDS);
    }

    //释放锁
    public void unLock() throws Exception {
        try {
            //删除锁节点
            zooKeeper.delete(lockNode, -1);
            System.out.println("线程：" + Thread.currentThread().getName() + ",解锁成功，节点名称：" + lockNode);
            lockNode = null;
            zooKeeper.close();
        } catch (Exception e) {
            throw new Exception(e);
        }

    }


}
