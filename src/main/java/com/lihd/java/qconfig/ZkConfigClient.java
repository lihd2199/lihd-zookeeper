package com.lihd.java.qconfig;

import com.lihd.java.Const;
import com.lihd.java.PropertiesUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @program: zookeeper-sample
 * @description:
 * @author: li_hd
 * @create: 2020-03-11 18:18
 **/
public class ZkConfigClient implements Runnable {

    private String nodePath = "/commConfig";

    private CommonConfig commonConfig;

    @Override
    public void run() {

        ZooKeeper zkClient = null;
        try {
            zkClient = new ZooKeeper(PropertiesUtils.readValue(Const.CONNECT_STRING), 3000, watchedEvent -> {
            });

            while (zkClient.exists(nodePath,false) == null) {
                System.out.println("配置节点不存在!");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取节点
            String data = Arrays.toString(zkClient.getData(nodePath, false, null));
            System.out.println(commonConfig.toString());

//            zkClient.getData(nodePath, new IZkDataListener() {
//
//                @Override
//                public void handleDataDeleted(String dataPath) throws Exception {
//                    if (dataPath.equals(nodePath)) {
//                        System.out.println("节点：" + dataPath + "被删除了！");
//                    }
//                }
//
//                @Override
//                public void handleDataChange(String dataPath, Object data) throws Exception {
//                    if (dataPath.equals(nodePath)) {
//                        System.out.println("节点：" + dataPath + ", 数据：" + data + " - 更新");
//                        commonConfig = (CommonConfig) data;
//                    }
//                }
//            });
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }


    }
}
