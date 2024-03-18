package com.inbank.decisionengine.service;

import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;

public interface DecisionService {
    Decision decide(DecisionRequest request);
}
