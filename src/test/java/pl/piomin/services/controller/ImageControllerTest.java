package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class ImageControllerTest {

    @Autowired
    private RestTestClient webTestClient;

//    @Test
    void testDescribe() {
        webTestClient.get()
            .uri("/images/describe")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String[].class)
            .value(list -> assertThat(list).isNotNull());
    }

    @Test
    void testDescribeImage() {
        // Use a known image id or fallback to a sample (e.g., "fruits")
        String imageId = "fruits";
        webTestClient.get()
            .uri("/images/describe/" + imageId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(body -> assertThat(body).isNotNull());
    }

//    @Test
    void testFindObject() {
        String object = "apple";
        webTestClient.get()
            .uri("/images/find/" + object)
            .exchange()
            .expectStatus().isOk()
            .expectBody(byte[].class)
            .value(body -> assertThat(body).isNotNull());
    }

//    @Test
//    void testGenerateImage() {
//        String object = "banana";
//        ResponseEntity<byte[]> response = restTemplate.getForEntity("/images/generate/" + object, byte[].class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        assertThat(response.getBody()).isNotNull();
//    }

//    @Test
//    void testGenerateAndMatch() {
//        String object = "orange";
//        ResponseEntity<String> response = restTemplate.getForEntity("/images/generate-and-match/" + object, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        assertThat(response.getBody()).isNotNull();
//    }
}
