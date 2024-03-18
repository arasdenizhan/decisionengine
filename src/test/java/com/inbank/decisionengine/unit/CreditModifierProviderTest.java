package com.inbank.decisionengine.unit;

import com.inbank.decisionengine.provider.impl.MockedCreditModifierProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditModifierProviderTest {
    @Test
    void testGetFromId() {
        MockedCreditModifierProvider provider = Mockito.mock(MockedCreditModifierProvider.class);

        Mockito.when(provider.getFromId("49002010965")).thenReturn(BigDecimal.ZERO);
        Mockito.when(provider.getFromId("49002010976")).thenReturn(BigDecimal.valueOf(100));
        Mockito.when(provider.getFromId("49002010987")).thenReturn(BigDecimal.valueOf(300));
        Mockito.when(provider.getFromId("49002010998")).thenReturn(BigDecimal.valueOf(1000));
        Mockito.when(provider.getFromId("non_existing_id")).thenReturn(BigDecimal.valueOf(-1));

        assertEquals(BigDecimal.ZERO, provider.getFromId("49002010965"));
        assertEquals(BigDecimal.valueOf(100), provider.getFromId("49002010976"));
        assertEquals(BigDecimal.valueOf(300), provider.getFromId("49002010987"));
        assertEquals(BigDecimal.valueOf(1000), provider.getFromId("49002010998"));
        assertEquals(BigDecimal.valueOf(-1), provider.getFromId("non_existing_id"));
    }
}
