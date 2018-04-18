package com.mycompany.transactions.service;

import com.mycompany.transactions.domain.Transaction;
import com.mycompany.transactions.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void should_add_transaction_when_timestamp_in_allowed_interval() {
        Transaction transaction = new Transaction(25.50, now());
        doNothing().when(transactionRepository).saveTransaction(transaction);

        boolean saved = transactionService.saveTransaction(transaction);

        verify(transactionRepository).saveTransaction(transaction);
        assertThat(saved).isTrue();
    }

    @Test
    public void should_not_add_transaction_when_timestamp_outof_allowed_interval() {
        Transaction transaction = new Transaction(25.50, now().minusSeconds(61));

        boolean saved = transactionService.saveTransaction(transaction);

        verify(transactionRepository, never()).saveTransaction(transaction);
        assertThat(saved).isFalse();
    }

}