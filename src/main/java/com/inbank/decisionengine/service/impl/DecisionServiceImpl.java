package com.inbank.decisionengine.service.impl;

import com.inbank.decisionengine.config.DecisionProperties;
import com.inbank.decisionengine.exception.CreditModifierNotFoundException;
import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;
import com.inbank.decisionengine.provider.CreditModifierProvider;
import com.inbank.decisionengine.service.DecisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.inbank.decisionengine.model.Decision.NEGATIVE_RESPONSE;
import static com.inbank.decisionengine.model.Status.DEBT;
import static com.inbank.decisionengine.model.Status.POSITIVE;
import static java.math.RoundingMode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {
    private final CreditModifierProvider mockedCreditModifierProvider;
    private final DecisionProperties decisionProperties;

    @Override
    public Decision decide(DecisionRequest request) {
        var requestedAmount = BigDecimal.valueOf(request.getAmount());
        var period = BigDecimal.valueOf(request.getPeriod());

        log.info("Calling provider for getting credit modifier.");
        var creditModifier = mockedCreditModifierProvider.getFromId(request.getId());
        log.info("Provider call for getting credit modifier is completed.");

        var checkResult = checkCreditModifier(creditModifier);
        if(Boolean.FALSE.equals(checkResult)){
            return Decision.prepareResponse(0, 0, DEBT);
        }

        var creditScore = calculateCreditScore(creditModifier, requestedAmount, period);
        var resultAmount = creditScore.multiply(requestedAmount);
        var resultPeriod = request.getPeriod();

        if(isCreditScoreGreaterThanOne(creditScore)){
            log.info("Credit score is greater than one, will return without calculating a new amount value.");
            resultAmount = decisionProperties.getMaxAmount().compareTo(resultAmount) < 0 ? decisionProperties.getMaxAmount() : resultAmount;
        } else if(isNewPeriodCalculationRequired(creditScore, resultAmount)) {
            log.info("New period calculation is required. Will calculate a new period.");
            resultPeriod = requestedAmount.divide(creditModifier, 10, HALF_UP).intValue();
            if(resultPeriod > decisionProperties.getMaxPeriod()) {
                log.info("Valid period not exists. Decision is negative!");
                return NEGATIVE_RESPONSE;
            }
            resultAmount = requestedAmount;
            log.info("New period calculation completed. Before = {}, After = {}", request.getPeriod(), resultPeriod);
        }
        return Decision.prepareResponse(resultAmount.intValue(), resultPeriod, POSITIVE);
    }

    private boolean checkCreditModifier(BigDecimal creditModifier){
        if(BigDecimal.ZERO.equals(creditModifier)){
            log.info("Requested user has debt. Returning NEGATIVE as result");
            return false;
        } else if (BigDecimal.valueOf(-1).equals(creditModifier)){
            log.error("Request user not found in credit modifier provider.");
            throw new CreditModifierNotFoundException("User not exists in credit modifier provider.");
        }
        return true;
    }

    private BigDecimal calculateCreditScore(BigDecimal modifier, BigDecimal amount, BigDecimal period){
        return (modifier.divide(amount, 10, HALF_UP)).multiply(period);
    }

    private boolean isCreditScoreGreaterThanOne(BigDecimal creditScore){
        return BigDecimal.ONE.compareTo(creditScore) <= 0;
    }

    private boolean isNewPeriodCalculationRequired(BigDecimal creditScore, BigDecimal resultAmount){
        return BigDecimal.ONE.compareTo(creditScore) > 0  && decisionProperties.getMinAmount().compareTo(resultAmount) > 0;
    }
}
