package com.cosmos.test;

import com.cosmos.exception.CheckoutException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * The type Completable futures tests.
 *
 * @author ambujmehra
 */
@Slf4j
public class CompletableFuturesTests {
    /**
     * The Executor service.
     */
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    static {
        log.info("Completable futures implements Futures and completion stage, using which you can register callback" +
                "There are concepts of promise Java 9 introduced Flowable and Subscriable push/pull model" +
                "Rx java has listenable for asynch proctaming");
        log.info("Discuss on fork join pool, its has a set of worker thread so context swith and creation and destruction of " +
                "threads is not a over head., discuss fork join pool algorithm and pros and cons of pool");

        log.info("show example of  partner integration\n");

    }

    /**
     * Blocking call.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void blockingCall() {
        String blockingResult = mostRecentDataOnSearch("java");
        log.info("Most result :: " + blockingResult);

    }

    /**
     * Executor service test.
     *
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     * @throws TimeoutException     the timeout exception
     */
    @Test
    public void executorServiceTest() throws InterruptedException, ExecutionException, TimeoutException {

        Callable<String> task = () -> mostRecentDataOnSearch("java");
        Future<String> javaResultFuture = executorService.submit(task);
        String result = javaResultFuture.get(4, TimeUnit.SECONDS);

        log.info("received result from task :: " + result);
        log.info("There are limited functionality with futures and not enuf APi, show API of Completable futures");
    }


    /**
     * Wait for first to finish.
     *
     * @throws Exception the exception
     */
    public void waitForFirstToFinish() throws Exception{
        Callable<String> taskJava = () -> mostRecentDataOnSearch("java");
        Callable<String> taskPython = () -> mostRecentDataOnSearch("pthon");
        log.info("no easy APi to check which one fines earlier need to do in a infinite loop and wait for earlier oe to give results");
        while (true) {
            // wastes cpu as well as thread
            executorService.submit(taskJava).get(1, TimeUnit.MICROSECONDS);
            executorService.submit(taskPython).get(1, TimeUnit.MICROSECONDS);
        }
    }

    /**
     * Wait for firct completable future.
     *
     * @throws Exception the exception
     */
    @Test
    public void waitForFirctCompletableFuture() throws Exception{
        log.info("No callback hells register callbacks and play with promises");

        CompletableFuture<String> java = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("java"));
        log.info("Overloaded version also takes a executor service as input");
        log.info("Supply synch is a supplier it returns a value and run asynch is s consumer, show fork join pool class" +
                "and completabe future API in class");
        log.info("result we have blocked main thread " + java.get());
    }

    /**
     * Non blocked waiting via call back.
     *
     * @throws Exception the exception
     */
    @Test
    public void nonBlockedWaitingViaCallBack() throws Exception{
        log.info("we can register a callback a non blocking invokation and chaining");
        CompletableFuture<String> java = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("java"));
        log.debug("This is from main thread asynch1");
        CompletableFuture<String> javaModified = java.thenApply(javaFuture -> {
            log.info("1st stage data is  :: " + javaFuture);
            return javaFuture + "Modifiction";
        });
        log.debug("This is from main thread asynch2");
        CompletableFuture<Integer> completableFutureLength = javaModified.thenApply(String::length);
        log.debug("This is from main thread asynch3");

        log.debug("Result of non blocking is and no callback HELLLLL!!! :" + completableFutureLength.get());
        log.info("Then Apply returns a Completable Future so we can chain the stages and is non blocking");
        log.info("Sho API of then cmpose , then apply, then accept , which ever stage returns CompletableFuture is non blocking");

    }


    /**
     * Join multiple threads result.
     *
     * @throws Exception the exception
     */
    @Test
    public void joinMultipleThreadsResult() throws Exception{
        CompletableFuture<String> java = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("java"));
        CompletableFuture<String> scala = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("scala"));
        CompletableFuture<String> python = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("python"));

        CompletableFuture<Void> allCompleted = CompletableFuture.allOf(java, scala, python);

        allCompleted.thenRun(() ->  {
            try {
                log.info("Java result" + java.get());
                log.info("Python result" + python.get());
                log.info("Scala result" + scala.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }).get();

        log.info("Ask what happens when we remove get from above??");

        log.debug("Show API of eitherOf, allOF, eitherof, firstOf, anyOf etc");
    }


    /**
     * First thread of two completes.
     *
     * @throws Exception the exception
     */
    @Test
    public void firstThreadOfTwoCompletes() throws Exception{
        // demo code

    }

    @Test
    public void exceptionHandling() throws Exception{
        log.info("Futures can have 2 result of type T or exception which is result of future add exception to method downstream");
        CompletableFuture<String> java = CompletableFuture.supplyAsync(() -> mostRecentDataOnSearch("java"));

        CompletableFuture<String> javaModified = java.thenApply(javaFuture -> {
            log.info("1st stage data is  :: " + javaFuture);
            return javaFuture + "Modifiction";
        }).exceptionally(throwable -> {
            log.error(throwable.getMessage());
            return "sorry";
        });

        CompletableFuture<Integer> completableFutureLength = javaModified.thenApply(String::length).exceptionally(throwable -> 0);
        log.debug("Result of non blocking is and no callback HELLLLL!!! :" + completableFutureLength.get());

        log.debug("HANDLE API also availableits ugly like we have in java script");
        //.handle(throwable, result)

    }


    /**
     * Most recent data on search string.
     *
     * @param text the text
     * @return the string
     * @throws InterruptedException the interrupted exception
     */
    public String mostRecentDataOnSearch(String text) {
        try {
            if(text.startsWith("j")) {
                Thread.sleep(2000);
            } else {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            throw new CheckoutException("",e);

        }

        return text + "result";
    }

}
