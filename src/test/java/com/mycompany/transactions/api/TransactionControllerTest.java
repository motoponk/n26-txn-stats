package com.mycompany.transactions.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.transactions.domain.Transaction;
import com.mycompany.transactions.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.Instant.now;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void should_get_statuscode_201_when_txn_is_within_allowed_interval() throws Exception {
        Transaction transaction = new Transaction(25.50, now());
        given(transactionService.saveTransaction(transaction)).willReturn(true);

        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(transaction))
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(content().string(""))
                .andExpect(status().isCreated())
                ;

        verify(transactionService).saveTransaction(transaction);
    }

    @Test
    public void should_get_statuscode_204_when_txn_is_outof_allowed_interval() throws Exception {
        Transaction transaction = new Transaction(25.50, now().minusSeconds(61));
        given(transactionService.saveTransaction(transaction)).willReturn(false);

        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(transaction))
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(content().string(""))
                .andExpect(status().isNoContent())
                ;

        verify(transactionService).saveTransaction(transaction);
    }
}