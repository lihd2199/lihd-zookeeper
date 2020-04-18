package com.lihd.java;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: lihd-zookeeper
 * @description: zk lock test
 * @author: li_hd
 * @create: 2020-04-18 17:26
 **/
public class LockTest {


    @Test
    public void test() throws Exception {

        Integer sessionTimeout = Integer.valueOf(PropertiesUtils.readValue("sessionTimeout"));

        String connectString = PropertiesUtils.readValue("connectString");



        final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        final CountDownLatch countDownLatch = new CountDownLatch(5);

        final ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                try {

                    cyclicBarrier.await();

                    Lock lock = new Lock(sessionTimeout, connectString);

                    lock.init();

                    System.out.println(Thread.currentThread().getName()+" before:" + System.currentTimeMillis());

                    lock.lock("lihd");

                    System.out.println(Thread.currentThread().getName()+" locking:" + System.currentTimeMillis());

                    lock.unLock();

                    System.out.println(Thread.currentThread().getName()+" after:" + System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }

            });
        }

        countDownLatch.await();

        executorService.shutdown();

    }


}
