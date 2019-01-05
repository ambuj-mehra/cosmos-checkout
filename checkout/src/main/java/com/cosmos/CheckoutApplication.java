package com.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The type Checkout application.
 *
 * @author ambujmehra
 */
@SpringBootApplication
@EnableFeignClients
public class CheckoutApplication {

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
        SpringApplication.run(CheckoutApplication.class, args);
    }
}
