package com.mycompany.transactions.api;

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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TransactionControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_save_txn_and_respond_with_201_code_when_txn_is_within_allowed_interval() {
        Transaction txn1 = new Transaction(60, now());
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/transactions", txn1, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    public void should_not_save_txn_and_respond_with_204_code_when_txn_is_outof_allowed_interval() {
        Transaction txn1 = new Transaction(60, now().minusSeconds(61));
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/transactions", txn1, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NO_CONTENT);
    }
}
