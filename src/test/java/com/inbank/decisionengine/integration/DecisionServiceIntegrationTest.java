package com.inbank.decisionengine.integration;

import com.inbank.decisionengine.config.DecisionProperties;
import com.inbank.decisionengine.exception.CreditModifierNotFoundException;
import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;
import com.inbank.decisionengine.model.Status;
import com.inbank.decisionengine.service.impl.DecisionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DecisionServiceIntegrationTest {

    @Autowired
    private DecisionServiceImpl decisionService;

    @MockBean
    private DecisionProperties decisionProperties;

    @Test
    void testDecide_PositiveCase() {
        DecisionRequest request = new DecisionRequest("49002010976", 4000, 12);
        when(decisionProperties.getMaxAmount()).thenReturn(BigDecimal.valueOf(10000));
        when(decisionProperties.getMinAmount()).thenReturn(BigDecimal.valueOf(2000));
        when(decisionProperties.getMaxPeriod()).thenReturn(40);
        when(decisionProperties.getMaxPeriod()).thenReturn(60);

        Decision decision = decisionService.decide(request);

        assertEquals(4000, decision.getAmount());
        assertEquals(40, decision.getPeriod());
        assertEquals(Status.POSITIVE, decision.getStatus());
    }

    @Test
    void testDecide_NegativeCase_UserHasDebt() {
        DecisionRequest request = new DecisionRequest("49002010965", 1000, 12);

        Decision decision = decisionService.decide(request);

        assertEquals(0, decision.getAmount());
        assertEquals(0, decision.getPeriod());
        assertEquals(Status.DEBT, decision.getStatus());
    }

    @Test
    void testDecide_NegativeCase_UserNotFound() {
        DecisionRequest request = new DecisionRequest("NonExistingUser", 1000, 12);

        assertThrows(CreditModifierNotFoundException.class, () -> decisionService.decide(request));
    }
}
