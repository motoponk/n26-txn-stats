package com.mycompany.transactions.service;

import com.mycompany.transactions.domain.Statistics;
import com.mycompany.transactions.domain.Transaction;
import com.mycompany.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.time.Instant.now;
import static java.util.stream.Collectors.summarizingDouble;

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

    public Statistics getStatistics() {
        List<Transaction> transactions = this.transactionRepository.getRecentTransactions(TRANSACTION_TIME_INTERVAL);
        DoubleSummaryStatistics summaryStatistics =
                transactions.parallelStream()
                            .collect(summarizingDouble(Transaction::getAmount));

        Statistics statistics = new Statistics(
                summaryStatistics.getSum(),
                summaryStatistics.getAverage(),
                getMax(summaryStatistics),
                getMin(summaryStatistics),
                summaryStatistics.getCount()
        );

        log.info("Statistics at Time : {} is {}", Instant.now(), statistics);
        return statistics;
    }

    private double getMax(DoubleSummaryStatistics stats) {
        final double max = stats.getMax();
        return NEGATIVE_INFINITY == max ? 0 : max;
    }

    private double getMin(DoubleSummaryStatistics stats) {
        final double min = stats.getMin();
        return POSITIVE_INFINITY == min ? 0 : min;
    }
}
