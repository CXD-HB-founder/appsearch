/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: FileScanTask
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/14 17:07
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/14

 * @since 1.0.0

 */

public class FileScanTask {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final CountDownLatch latch = new CountDownLatch(1);
    //private static int count;
    private AtomicInteger count = new AtomicInteger();

    private FileScanCallack calback;

    public FileScanTask(FileScanCallack calback){
        this.calback = calback;
    }
    public void startScan(File root) {
        count.incrementAndGet();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                list(root);
            }
        });
    }

    public void waitFinsh() throws InterruptedException {
        try {
            latch.await();
        } finally {
            pool.shutdown();//调用interrupt（）关闭
            //shutdownNow（）调用stop()关闭；
        }
    }

    public void list(File dir) {
        if (!Thread.interrupted()) {
            try {
                calback.execute(dir);
                if (dir.isDirectory()) {
                    File[] children = dir.listFiles();
                    if (children != null && children.length > 0) {
                        for (File child : children) {
                            if (child.isDirectory()) {
                                count.incrementAndGet();
                                pool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                calback.execute(child);
                            }
                        }
                    }
                }
            } finally {
                if (count.decrementAndGet() == 0) {
                    latch.countDown();
                }
            }
        }
    }
}