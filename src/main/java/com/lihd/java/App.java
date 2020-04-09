package com.lihd.java;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Hello world!
 */
public class App {


    private ZooKeeper zkClient;

    @Test
    public void getClient() throws IOException, KeeperException, InterruptedException {


        String connectString = PropertiesUtils.readValue("connectString");

        int sessionTimeout = Integer.parseInt(Objects.requireNonNull(PropertiesUtils.readValue("sessionTimeout")));

        zkClient = new ZooKeeper(Objects.requireNonNull(connectString), sessionTimeout, watchedEvent -> {

//            final String path = watchedEvent.getPath();
//
//            System.out.println(path);

        });

        System.out.println(Arrays.toString(zkClient.getData("/lihd", false, null)));


        zkClient.create("/name","lihd".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);

    }



}
