package com.mycompany.transactions.repository;

import com.mycompany.transactions.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class TransactionRepository {

    private final List<Transaction> TRANSACTIONS = new ArrayList();

    public void saveTransaction(Transaction txn) {
        TRANSACTIONS.add(txn);
        if(log.isTraceEnabled()) {
            printTransactions();
        }
    }

    private void printTransactions() {
        log.info(TRANSACTIONS.toString());
    }

    public List<Transaction> getRecentTransactions(long txnTimeInterval) {
        return TRANSACTIONS.stream()
                .filter(txn -> Duration.between(txn.getTimestamp(), now()).getSeconds() <= txnTimeInterval)
                .collect(toList());
    }
}
