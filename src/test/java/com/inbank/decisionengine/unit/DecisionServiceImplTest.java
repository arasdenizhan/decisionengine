package com.inbank.decisionengine.unit;

import com.inbank.decisionengine.config.DecisionProperties;
import com.inbank.decisionengine.exception.CreditModifierNotFoundException;
import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;
import com.inbank.decisionengine.model.Status;
import com.inbank.decisionengine.provider.CreditModifierProvider;
import com.inbank.decisionengine.service.impl.DecisionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DecisionServiceImplTest {

    @Mock
    private CreditModifierProvider mockedCreditModifierProvider;

    @Mock
    private DecisionProperties decisionProperties;

    @InjectMocks
    private DecisionServiceImpl decisionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(decisionProperties.getMinAmount()).thenReturn(BigDecimal.valueOf(2000));
        when(decisionProperties.getMaxAmount()).thenReturn(BigDecimal.valueOf(10000));
        when(decisionProperties.getMinPeriod()).thenReturn(12);
        when(decisionProperties.getMaxPeriod()).thenReturn(60);
    }

    @Test
    void testDecideWithZeroCreditScore() {
        DecisionRequest request = new DecisionRequest("49002010976", 10000, 12);
        when(mockedCreditModifierProvider.getFromId("49002010976")).thenReturn(BigDecimal.valueOf(100));

        Decision decision = decisionService.decide(request);

        assertEquals(Status.NEGATIVE, decision.getStatus());
        assertEquals(0, decision.getAmount());
        assertEquals(0, decision.getPeriod());
    }

    @Test
    void testDecideWithDebtCreditModifier() {
        DecisionRequest request = new DecisionRequest("49002010965", 2500, 12);
        when(mockedCreditModifierProvider.getFromId("49002010965")).thenReturn(BigDecimal.ZERO);

        Decision decision = decisionService.decide(request);

        assertEquals(Status.DEBT, decision.getStatus());
        assertEquals(0, decision.getAmount());
        assertEquals(0, decision.getPeriod());
    }

    @Test
    void testDecideWithNotFoundCreditModifier() {
        DecisionRequest request = new DecisionRequest("non_existing_id", 1000, 12);
        when(mockedCreditModifierProvider.getFromId("non_existing_id")).thenReturn(BigDecimal.valueOf(-1));

        assertThrows(CreditModifierNotFoundException.class, () -> decisionService.decide(request));
    }

    @Test
    void testDecideWithCreditScoreGreaterThanOne() {
        DecisionRequest request = new DecisionRequest("49002010976", 2500, 25);
        when(mockedCreditModifierProvider.getFromId("49002010976")).thenReturn(BigDecimal.valueOf(100));

        Decision decision = decisionService.decide(request);

        assertEquals(Status.POSITIVE, decision.getStatus());
        assertEquals(2500, decision.getAmount());
        assertEquals(25, decision.getPeriod());
    }

    @Test
    void testDecideWithNewPeriodCalculation() {
        DecisionRequest request = new DecisionRequest("49002010976", 4000, 12);
        when(mockedCreditModifierProvider.getFromId("49002010976")).thenReturn(BigDecimal.valueOf(100));

        Decision decision = decisionService.decide(request);

        assertEquals(Status.POSITIVE, decision.getStatus());
        assertEquals(4000, decision.getAmount());
        assertEquals(40, decision.getPeriod());
    }
}