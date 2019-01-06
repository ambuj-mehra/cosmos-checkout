package com.cosmos.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Streams tests.
 *
 * @author ambujmehra
 */
@Slf4j
public class StreamsTests {


    private Ledger tonyStarkLedger = null;

    /**
     * Create ledger.
     */
    @Before
    public void createLedger() {

        log.info("Streams are powerful java 8 features which use fok join pool to distribute tasks example of assembly line");
        List<Ledger.Transaction> credits = Arrays.asList(
                new Ledger.Transaction("CREDIT", BigDecimal.valueOf(500), LocalDate.now(), "Thor"),
                new Ledger.Transaction("CREDIT", BigDecimal.valueOf(1000), LocalDate.now(), "Black Widow"),
                new Ledger.Transaction("CREDIT", BigDecimal.valueOf(400), LocalDate.now(), "Black Widow"),
                new Ledger.Transaction("CREDIT", BigDecimal.valueOf(335), LocalDate.now(), "CaptainAmerica")
        );

        List<Ledger.Transaction> debits = Arrays.asList(
                new Ledger.Transaction("DEBIT", BigDecimal.valueOf(500), LocalDate.now(), "Hulk"),
                new Ledger.Transaction("DEBIT", BigDecimal.valueOf(10), LocalDate.now(), "Thor"),
                new Ledger.Transaction("DEBIT", BigDecimal.valueOf(400), LocalDate.now(), "Captain America"),
                new Ledger.Transaction("DEBIT", BigDecimal.valueOf(335), LocalDate.now(), "Hulk")
                );

        tonyStarkLedger = Ledger.builder()
                .firstName("Tony")
                .lastName("Stark")
                .credits(credits)
                .debits(debits)
                .build();

    }

    /**
     * Sort tony debits based on amount.
     */
    @Test
    public void sortTonyDebitsBasedOnAmount() {
        log.info("Comparator is a functional interface!!");
        Collections.sort(tonyStarkLedger.getCredits(), ((o1, o2) -> o1.getAmount().compareTo(o2.getAmount()))); //convert to inline
        log.info("updated ledger :: " + tonyStarkLedger.toString());
    }

    /**
     * Sort debits and prints the ledger and find hulk amount.
     */
    @Test
    public void sortDebitsAndPrintsTheLedgerAndFindHulkAmount() {
        log.info("in case of 2 or more operations we can chain them one after other like a assembly line");

        log.info("map stage is user to transfrom 1 onbect to another and in next stage it become stram of transformed object");
        tonyStarkLedger.getDebits().stream()
                .sorted(Comparator.comparing(Ledger.Transaction::getAmount))
                .map( transaction -> {
                    log.info("transaction of debit for  :: {} and amount  {}" , transaction.getTransactingPerson(), transaction.getAmount());
                    return transaction;
                })
                .filter(transaction -> transaction.getTransactingPerson().equals("Hulk"))
                .collect(Collectors.toList());

        log.info("ask to predict the return type of above stream and the add inline reduce");


        BigDecimal hulkMoney = tonyStarkLedger.getDebits().stream()
                .filter(transaction -> transaction.getTransactingPerson().equals("Hulk"))   // list of trasactions
                .map(Ledger.Transaction::getAmount)  // list of bigdecimals
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("show differ bigdecimal operations like max min and remove teh map stage for demo");
        log.info("hilk debits " + hulkMoney);


    }

    /**
     * Send money to zaak pay for debit of thor.
     */
    @Test
    public void sendMoneyToZaakPayForDebitOfThor() {
        //demo code

        // demo examples from equals and hash code in tournament dto in core

        //show Orderstate enum for example also

    }

}
