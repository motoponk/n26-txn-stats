package com.mycompany.transactions.api;

import com.mycompany.transactions.domain.Statistics;
import com.mycompany.transactions.domain.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StatisticsControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_get_stats_for_registered_transactions() {
        Transaction txn1 = new Transaction(60, now());
        Transaction txn2 = new Transaction(40, now());
        Statistics expectedStatistics = new Statistics(100, 50, 60, 40, 2);

        restTemplate.postForEntity("/transactions", txn1, Void.class);
        restTemplate.postForEntity("/transactions", txn2, Void.class);
        ResponseEntity<Statistics> responseEntity = restTemplate.getForEntity("/statistics", Statistics.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStatistics);
    }
}
