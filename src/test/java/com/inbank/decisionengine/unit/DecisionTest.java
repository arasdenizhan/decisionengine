package com.inbank.decisionengine.unit;

import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecisionTest {
    @Test
    void testPrepareResponse() {
        Integer amount = 1000;
        Integer period = 12;
        Status status = Status.POSITIVE;

        Decision decision = Decision.prepareResponse(amount, period, status);

        assertEquals(amount, decision.getAmount());
        assertEquals(period, decision.getPeriod());
        assertEquals(status, decision.getStatus());
    }

    @Test
    void testNegativeResponse() {
        Decision decision = Decision.NEGATIVE_RESPONSE;
        assertEquals(0, decision.getAmount());
        assertEquals(0, decision.getPeriod());
        assertEquals(Status.NEGATIVE, decision.getStatus());
    }
}
