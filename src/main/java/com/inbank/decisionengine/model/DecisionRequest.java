package com.inbank.decisionengine.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DecisionRequest {
    @NotNull
    private String id;

    @NotNull
    @Min(2000)
    @Max(10000)
    private Integer amount;

    @NotNull
    @Min(12)
    @Max(60)
    private Integer period;
}
