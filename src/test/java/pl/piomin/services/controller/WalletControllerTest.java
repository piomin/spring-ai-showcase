package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testWalletValueWithTools() {
        String body = webTestClient.get()
                .uri("/wallet/with-tools")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(body).isNotNull();
    }

//    @Test
//    void testHighestWalletValue() {
//        int days = 5;
//        String body = webTestClient.get()
//                .uri("/wallet/highest-day/" + days)
//                .exchange()
//                .expectStatus().is2xxSuccessful()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//        assertThat(body).isNotNull();
//    }
}
