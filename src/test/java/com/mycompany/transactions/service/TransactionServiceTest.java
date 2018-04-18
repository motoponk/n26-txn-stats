package com.mycompany.transactions.service;

import com.mycompany.transactions.domain.Statistics;
import com.mycompany.transactions.domain.Transaction;
import com.mycompany.transactions.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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

    @Test
    public void should_return_stats_when_there_are_txns_in_allowed_interval() {
        List<Transaction> txns = asList(
                new Transaction(15, now().minusSeconds(10)),
                new Transaction(25, now().minusSeconds(20)),
                new Transaction(20, now().minusSeconds(50))
        );
        given(transactionRepository.getRecentTransactions(60)).willReturn(txns);

        Statistics statistics = transactionService.getStatistics();

        Statistics expected = new Statistics(60,20,25,15,3);

        verify(transactionRepository).getRecentTransactions(60);
        assertThat(statistics).isEqualToComparingFieldByField(expected);
    }

}