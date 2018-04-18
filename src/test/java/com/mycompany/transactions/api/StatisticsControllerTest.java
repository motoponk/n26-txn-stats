package com.mycompany.transactions.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.transactions.domain.Statistics;
import com.mycompany.transactions.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StatisticsController.class)
public class StatisticsControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void should_return_stats_when_txns_exists_in_allowed_range() throws Exception {
        Statistics expectedStatistics = new Statistics(60,20,25,15,3);
        given(transactionService.getStatistics()).willReturn(expectedStatistics);

        MvcResult result = mockMvc.perform(get("/statistics")
                            .accept(APPLICATION_JSON_UTF8))
                            .andExpect(status().isOk())
                            .andReturn();
        Statistics actualStatistics = mapper.readValue(result.getResponse().getContentAsString(), Statistics.class);

        verify(transactionService).getStatistics();
        assertThat(actualStatistics).isEqualToComparingFieldByField(expectedStatistics);
    }
}