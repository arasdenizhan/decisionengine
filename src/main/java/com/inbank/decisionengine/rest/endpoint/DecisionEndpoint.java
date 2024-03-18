package com.inbank.decisionengine.rest.endpoint;

import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;
import com.inbank.decisionengine.service.DecisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/decision-api/v1/decisions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DecisionEndpoint {
    private final DecisionService decisionService;
    @PostMapping
    public ResponseEntity<Decision> decide(@Valid @RequestBody DecisionRequest decisionRequest){
        return ResponseEntity.ok(decisionService.decide(decisionRequest));
    }
}
