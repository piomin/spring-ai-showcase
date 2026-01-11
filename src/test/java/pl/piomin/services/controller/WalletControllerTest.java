package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class WalletControllerTest {

    @Autowired
    private RestTestClient webTestClient;

    @Test
    void testWalletValueWithTools() {
        webTestClient.get()
            .uri("/wallet/with-tools")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(body -> assertThat(body).isNotNull());
    }

//    @Test
//    void testHighestWalletValue() {
//        int days = 5;
//        webTestClient.get()
//            .uri("/wallet/highest-day/" + days)
//            .exchange()
//            .expectStatus().isOk()
//            .expectBody(String.class)
//            .value(body -> assertThat(body).isNotNull());
//    }
}
