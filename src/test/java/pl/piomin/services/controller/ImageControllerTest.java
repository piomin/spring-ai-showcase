package pl.piomin.services.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

//    @Test
    void testDescribe() {
        ResponseEntity<String[]> response = restClient.get()
                .uri("/images/describe")
                .retrieve()
                .toEntity(String[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testDescribeImage() {
        // Use a known image id or fallback to a sample (e.g., "fruits")
        String imageId = "fruits";
        ResponseEntity<String> response = restClient.get()
                .uri("/images/describe/" + imageId)
                .retrieve()
                .toEntity(String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

//    @Test
    void testFindObject() {
        String object = "apple";
        ResponseEntity<byte[]> response = restClient.get()
                .uri("/images/find/" + object)
                .retrieve()
                .toEntity(byte[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

//    @Test
//    void testGenerateImage() {
//        String object = "banana";
//        ResponseEntity<byte[]> response = restClient.get()
//                .uri("/images/generate/" + object)
//                .retrieve()
//                .toEntity(byte[].class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        assertThat(response.getBody()).isNotNull();
//    }

//    @Test
//    void testGenerateAndMatch() {
//        String object = "orange";
//        ResponseEntity<String> response = restClient.get()
//                .uri("/images/generate-and-match/" + object)
//                .retrieve()
//                .toEntity(String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        assertThat(response.getBody()).isNotNull();
//    }
}
