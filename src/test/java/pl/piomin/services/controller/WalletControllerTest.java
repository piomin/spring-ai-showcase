package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testWalletValue() {
        ResponseEntity<String> response = restTemplate.getForEntity("/wallet", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testWalletValueWithTools() {
        ResponseEntity<String> response = restTemplate.getForEntity("/wallet/with-tools", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testHighestWalletValue() {
        int days = 5;
        ResponseEntity<String> response = restTemplate.getForEntity("/wallet/highest-day/" + days, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }
}
