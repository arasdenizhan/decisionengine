package com.inbank.decisionengine.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Component
public class DecisionProperties {
    @Value("${decision.min.amount}")
    private BigDecimal minAmount;
    @Value("${decision.min.period}")
    private Integer minPeriod;
    @Value("${decision.max.amount}")
    private BigDecimal maxAmount;
    @Value("${decision.max.period}")
    private Integer maxPeriod;
}
