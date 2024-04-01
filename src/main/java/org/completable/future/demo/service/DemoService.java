package org.completable.future.demo.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

@Service
public class DemoService {

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public void testMainThread() {
        executorService.submit( () -> {
            logger.info("Hello from Test Main thread {} ", Thread.currentThread().getName());
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Bye from Test Main thread {} ", Thread.currentThread().getName());
        });
    }


    /** Not Used...
    @Async
    public Future<String> testCompletableFutureAsync() {
        logger.info("Hello from Test Async {} ", Thread.currentThread().getName());
        CompletableFuture<String> future = completableFutureDemo();
        while(!future.isDone()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Task Not complete");
        }
        logger.info("Bye from Test Async {} ", Thread.currentThread().getName());
        return future;
    }

    private CompletableFuture<String> completableFutureDemo() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Hello from CompletableFuture Async {} ", Thread.currentThread().getName());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "completableFutureDemo";
        });
        return future;
    }
    */



    public Future<CompletableFuture<String>> testCompletableFutureWithExecutor() {
        logger.info("Main Thread Starts {} ", Thread.currentThread().getName());
        Future<CompletableFuture<String>> future =  executorService.submit( () -> {
            logger.info("Hello from Executor Service thread {} ", Thread.currentThread().getName());
            Thread.sleep(10000);
            CompletableFuture<String> result =  completableFutureDemo2();
            logger.info("Bye from Executor Service thread {} ", Thread.currentThread().getName());
            return result;
        });
        logger.info("Main Thread Ends {} ", Thread.currentThread().getName());
        return future;
    }

    private CompletableFuture<String> completableFutureDemo2() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Hello from CompletableFuture thread {} ", Thread.currentThread().getName());
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            logger.info("Bye from CompletableFuture thread {} ", Thread.currentThread().getName());
            return "completableFutureDemo";
        }, executorService);
        return future;
    }

    public Future<ListenableFuture<String>> testListenableFutureWithExecutor() {
        logger.info("Main Thread Starts {} ", Thread.currentThread().getName());
        Future<ListenableFuture<String>> future =  executorService.submit( () -> {
            logger.info("Hello from Executor Service thread {} ", Thread.currentThread().getName());
            ListenableFuture<String> result =  listenableFutureDemo();
            Futures.addCallback(result, new FutureCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    logger.info("Call back Success");
                }

                @Override
                public void onFailure(Throwable t) {
                    logger.info("Call Back Failed");
                }
            }, executorService);
            logger.info("Bye from Executor Service thread {} ", Thread.currentThread().getName());
            return result;
        });
        logger.info("Main Thread Ends {} ", Thread.currentThread().getName());
        return future;
    }

    private ListenableFuture<String> listenableFutureDemo() {
        ListenableFuture<String> listenableFuture = listeningExecutorService.submit( () -> {
            logger.info("Hello from ListenableFuture thread {} ", Thread.currentThread().getName());
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            logger.info("Bye from ListenableFuture thread {} ", Thread.currentThread().getName());
            return "ListenableFutureDemo";
        });
        return listenableFuture;
    }
}
