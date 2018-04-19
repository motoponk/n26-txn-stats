package com.mycompany.transactions.repository;

import com.mycompany.transactions.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class TransactionRepository {

    private final ConcurrentNavigableMap<Long, List<Transaction>> TRANSACTIONS = new ConcurrentSkipListMap();

    protected NavigableMap<Long, List<Transaction>> getTransactions() {
        return Collections.unmodifiableNavigableMap(TRANSACTIONS);
    }

    public void saveTransaction(Transaction txn) {
        long timestamp = txn.getTimestamp().toEpochMilli();
        if(!TRANSACTIONS.containsKey(timestamp)){
            TRANSACTIONS.put(timestamp, new ArrayList());
        }
        TRANSACTIONS.get(timestamp).add(txn);

        if(log.isTraceEnabled()) {
            printTransactions();
        }
    }

    private void printTransactions() {
        log.info(TRANSACTIONS.toString());
    }

    public List<Transaction> getRecentTransactions(long txnTimeIntervalInSeconds) {
        long startingEpochSecond = now().minusSeconds(txnTimeIntervalInSeconds).toEpochMilli();
        return TRANSACTIONS
                .tailMap(startingEpochSecond)
                .values()
                .parallelStream()
                .flatMap(Collection::parallelStream)
                .collect(toList());
    }
}
