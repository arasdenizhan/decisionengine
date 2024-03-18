package com.inbank.decisionengine.provider;

import java.math.BigDecimal;

public interface CreditModifierProvider {
    BigDecimal getFromId(String id);
}
