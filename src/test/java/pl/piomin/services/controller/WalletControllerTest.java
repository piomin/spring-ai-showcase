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
    private RestTestClient restTestClient;

    @Test
    void testWalletValueWithTools() {
        String body = restTestClient.get()
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
