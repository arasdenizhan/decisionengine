package com.inbank.decisionengine.provider.impl;

import com.inbank.decisionengine.provider.CreditModifierProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MockedCreditModifierProvider implements CreditModifierProvider {

    @Override
    public BigDecimal getFromId(String id) {
        return switch (id) {
            case "49002010965" -> BigDecimal.ZERO;
            case "49002010976" -> BigDecimal.valueOf(100);
            case "49002010987" -> BigDecimal.valueOf(300);
            case "49002010998" -> BigDecimal.valueOf(1000);
            default -> BigDecimal.valueOf(-1);
        };
    }
}
