package com.mycompany.transactions.repository;

import com.mycompany.transactions.domain.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.NavigableMap;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionRepositoryTest {
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    public void should_save_transaction() {
        assertThat(transactionRepository.getTransactions()).isEmpty();
        Transaction txn = new Transaction(25.50, now());
        transactionRepository.saveTransaction(txn);
        NavigableMap<Long, List<Transaction>> transactions = transactionRepository.getTransactions();
        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.values().stream().findFirst().get().get(0)).isEqualTo(txn);
    }

    @Test
    public void should_return_txns_with_timestamp_in_allowed_interval_only() {
        Transaction txn1 = new Transaction(25.50, now());
        Transaction txn2 = new Transaction(15.50, now().minusSeconds(10));
        Transaction txn3 = new Transaction(45.50, now().minusSeconds(61));

        transactionRepository.saveTransaction(txn1);
        transactionRepository.saveTransaction(txn2);
        transactionRepository.saveTransaction(txn3);

        assertThat(transactionRepository.getRecentTransactions(60).size()).isEqualTo(2);
    }
}