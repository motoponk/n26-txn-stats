package com.mycompany.transactions.service;

import com.mycompany.transactions.domain.Transaction;
import com.mycompany.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static java.time.Instant.now;

@Service
@Slf4j
public class TransactionService {

    private static final long TRANSACTION_TIME_INTERVAL = 60;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean saveTransaction(Transaction txn) {
        if(!transactionInValidTimeRange(txn)) {
            log.info("Received a transaction with timestamp older than {} seconds", TRANSACTION_TIME_INTERVAL);
            return false;
        }
        this.transactionRepository.saveTransaction(txn);
        log.info("Transaction saved successfully");
        return true;
    }

    private boolean transactionInValidTimeRange(Transaction txn) {
        long txnAgeInSeconds = Duration.between(txn.getTimestamp(), now()).getSeconds();
        log.info("Transaction age in Seconds: {}", txnAgeInSeconds);
        return txnAgeInSeconds <= TRANSACTION_TIME_INTERVAL;
    }

}
