package com.cosmos.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * O
 * The type Lambda tests.
 *
 * @author ambujmehra
 */
@Slf4j
public class LambdaTests {

    /**
     * Lamdba for passing logic to method.
     */
    @Test
    public void lamdbaForPassingLogicToMethod() {
        log.info("Behaviour in java 7 were always associated with an object");
        log.info("We pass a objec to a method and use the behaviour of the object that contains our business logic");
        log.info("Java 8 lets us pass functions as value based on types (Functional interface) Supplier/Consumer/predicate");

        Supplier<Integer> businessLogic = () -> "business logic".length();
        // update this to :: usage as shown by editor ^
        testSupplier(businessLogic);

        log.info("Explain Functional interface, interface runnable and callable are functional Interface DEMO");
    }

    private void testSupplier(Supplier<Integer> businessLogic) {
        log.info("Mylogic - " + businessLogic.get());

    }

    /**
     * My custom functional interface test.
     */
    @Test
    public void myCustomFunctionalInterfaceTest() {
        log.info("A functional interface is a interface with only 1 and only 1 abstarct behaviour");
        log.info("Example for custom functional Interface to add 3 Integers/ can be extended to generics");
        MyCustomLambda add3 = (a,b, c) -> a+b+c;
        log.info("My sum is ::" +  add3.add(1,2,3));
        log.info("Demo of Predicate/Consumer also functional interface are alternative of anonymous inner class but are not same demo later");
        log.info("Java designers user interface as lambda type for backwrd Compatibility, hence we can use interface like runnable and callable - DEMO");

    }

    /**
     * Consumer and predicate test.
     */
    @Test
    public void consumerAndPredicateTest() {
        log.info("Consumer is a interface that takes 1 value and emits nothing");
        Consumer<String> getLength = a -> log.info("My string length is :: " + a.length());
        getLength.accept("Merchants Online");


        log.info("Predicate is a interface that takes a value and tests it and returns a boolean value");
        Predicate<String> checksumValidator = merchantChecksum -> {
            String mobikwikChecksum = "xyz";
            return mobikwikChecksum.equals(merchantChecksum);
        };
        log.info("Checksum from merchant is valid? :: " + checksumValidator.test("xyz"));

    }

    /**
     * Live biconsumer demo.
     */
    @Test
    public void liveBiconsumerDemo() {
        log.info("Bi consumer is a interface that consumes 2 value and emist nothing");
        //Demo
    }


    /**
     * Runnable test.
     */
    @Test
    public void runnableTest() {
        log.info("Since runnable has a single method run it can be used as a functional Interface");
        log.info("Also a demo of anonymous inner class alternative in lambda");
        //old way
        Thread myOldThread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("My old  thread");
            }
        });
        myOldThread.run();

        // new better way

        Runnable myThread = () -> log.info("My lambda thread");

        Runnable myThreadWithSleep = () -> {
            log.info("My sleeping thread starts");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("My sleeping Thread ends ");
        };

        myThread.run();
        myThreadWithSleep.run();
    }

    /**
     * Live callable test.
     */
    @Test
    public void cllableTest() {
        log.info("Callables are great they let the thread return value, a FUTURe, this is a base of Completable future");
        //Demo
    }
}
