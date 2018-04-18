package com.mycompany.transactions.repository;

import com.mycompany.transactions.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

}
