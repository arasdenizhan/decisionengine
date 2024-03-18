package com.inbank.decisionengine.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.inbank.decisionengine.model.Decision;
import com.inbank.decisionengine.model.DecisionRequest;
import com.inbank.decisionengine.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class DecisionEndpointTest {
    private static final int PORT = 8089;
    private static final String API_PATH = "/decision-api/v1/decisions";

    private WireMockServer wireMockServer;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public void setUp() {
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
        WireMock.configureFor("localhost", PORT);

        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testDecideEndpoint() throws Exception {
        setUp();

        DecisionRequest request = new DecisionRequest("49002010976", 4000, 12);
        Decision expectedDecision = new Decision(4000, 40, Status.DEBT);

        stubFor(post(urlEqualTo("/decision-api/v1/decisions"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedDecision))));

        ResponseEntity<Decision> response = restTemplate.postForEntity("http://localhost:" + PORT + API_PATH, request, Decision.class);

        assert response.getStatusCode().is2xxSuccessful();
        assertEquals(expectedDecision, response.getBody());

        tearDown();
    }

    @Test
    void testDecideEndpointWithError() throws Exception {
        setUp();

        DecisionRequest request = new DecisionRequest("49002010976", 4000, 12);

        stubFor(post(urlEqualTo("/decision-api/v1/decisions"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));

        assertThrows(HttpServerErrorException.InternalServerError.class, () -> {
            restTemplate.postForEntity("http://localhost:" + PORT + API_PATH, request, Decision.class);
        });

        tearDown();
    }
}
