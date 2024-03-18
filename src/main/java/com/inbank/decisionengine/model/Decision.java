package com.inbank.decisionengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.inbank.decisionengine.model.Status.NEGATIVE;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Decision {
    private Integer amount;
    private Integer period;
    private Status status;

    public static final Decision NEGATIVE_RESPONSE = new Decision(0,0, NEGATIVE);

    public static Decision prepareResponse(Integer amount, Integer period, Status status){
        return Decision.builder()
                .amount(amount)
                .period(period)
                .status(status).build();
    }
}
