package com.mycompany.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Statistics {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;
}
