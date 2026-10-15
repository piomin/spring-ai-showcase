package pl.piomin.services.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import pl.piomin.services.model.Person;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("anthropic")
@DirtiesContext
class PersonControllerTestWithAnthropic {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @Order(1)
    void testFindAllPersons() {
        ResponseEntity<Person[]> response = restClient.get()
                .uri("/persons")
                .retrieve()
                .toEntity(Person[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).length).isEqualTo(10);
    }

    @Test
    @Order(2)
    void testFindPersonById() {
        int id = 4;
        ResponseEntity<Person> byIdResponse = restClient.get()
                .uri("/persons/" + id)
                .retrieve()
                .toEntity(Person.class);
        assertThat(byIdResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(byIdResponse.getBody()).isNotNull();
        assertThat(byIdResponse.getBody().getId()).isEqualTo(id);
    }

}
