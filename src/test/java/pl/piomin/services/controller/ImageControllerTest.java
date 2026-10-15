package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

//    @Test
    void testDescribe() {
        String[] body = webTestClient.get()
                .uri("/images/describe")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String[].class)
                .returnResult()
                .getResponseBody();
        assertThat(body).isNotNull();
    }

    @Test
    void testDescribeImage() {
        // Use a known image id or fallback to a sample (e.g., "fruits")
        String imageId = "fruits";
        webTestClient.get()
                .uri("/images/describe/" + imageId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotNull());
    }

//    @Test
    void testFindObject() {
        String object = "apple";
        byte[] body = webTestClient.get()
                .uri("/images/find/" + object)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(byte[].class)
                .returnResult()
                .getResponseBody();
        assertThat(body).isNotNull();
    }

//    @Test
//    void testGenerateImage() {
//        String object = "banana";
//        byte[] body = webTestClient.get()
//                .uri("/images/generate/" + object)
//                .exchange()
//                .expectStatus().is2xxSuccessful()
//                .expectBody(byte[].class)
//                .returnResult()
//                .getResponseBody();
//        assertThat(body).isNotNull();
//    }

//    @Test
//    void testGenerateAndMatch() {
//        String object = "orange";
//        String body = webTestClient.get()
//                .uri("/images/generate-and-match/" + object)
//                .exchange()
//                .expectStatus().is2xxSuccessful()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//        assertThat(body).isNotNull();
//    }
}
