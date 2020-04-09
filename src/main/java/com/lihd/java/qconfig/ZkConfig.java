package com.lihd.java.qconfig;

import com.lihd.java.PropertiesUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @program: zookeeper-sample
 * @description:
 * @author: li_hd
 * @create: 2020-03-11 18:10
 **/
public class ZkConfig {

    private String nodePath = "/commConfig";
    private CommonConfig commonConfig;
    private ZooKeeper zkClient;

    public CommonConfig initConfig(CommonConfig commonConfig) {
        if(commonConfig == null) {
            this.commonConfig = new CommonConfig("jdbc:mysql://127.0.0.1:3306/mydata?useUnicode=true&characterEncoding=utf-8",
                    "root", "root", "com.mysql.jdbc.Driver");
        } else {
            this.commonConfig = commonConfig;
        }
        return this.commonConfig;
    }

    /**
     * 更新配置
     *
     * @param commonConfig
     * @return
     */
    public CommonConfig update(CommonConfig commonConfig) throws IOException, KeeperException, InterruptedException {
        if(commonConfig != null) {
            this.commonConfig = commonConfig;
        }
        syncConfigToZookeeper();
        return this.commonConfig;
    }

    public void syncConfigToZookeeper() throws IOException, KeeperException, InterruptedException {
        if(zkClient == null) {
            zkClient = new ZooKeeper(PropertiesUtils.readValue("connectString"), 3000, watchedEvent -> {

            });
        }
        if(zkClient.exists(nodePath,false) == null) {
            zkClient.create(nodePath,nodePath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
    }



}
