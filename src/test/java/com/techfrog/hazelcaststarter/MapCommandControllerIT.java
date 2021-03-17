package com.techfrog.hazelcaststarter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MapCommandControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPutRequest() {
        //when
        WebTestClient.ResponseSpec responseSpec = makePostRequest("/map/put?key={key}&value={value}", "key1", "value1");

        //then
        responseSpec.expectStatus()
                .is2xxSuccessful()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.value").isEqualTo("value1");
    }

    private WebTestClient.ResponseSpec makePostRequest(String uri, String... parameters) {
        return webTestClient
                .post()
                .uri(uri, parameters)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange();
    }

    @Test
    void testGetResponse() {
        makePostRequest("/map/put?key={key}&value={value}", "key1", "value1");

        WebTestClient.ResponseSpec responseSpec = webTestClient
                .get()
                .uri("/replicated/get?key={key}", "key1")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange();

        responseSpec.expectStatus()
                .is2xxSuccessful()
                .expectHeader()
                .contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.value").isEqualTo("value1");
    }
}