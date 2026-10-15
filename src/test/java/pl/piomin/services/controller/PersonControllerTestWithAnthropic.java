package pl.piomin.services.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.piomin.services.model.Person;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("anthropic")
@DirtiesContext
class PersonControllerTestWithAnthropic {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testFindAllPersons() {
        Person[] persons = webTestClient.get()
                .uri("/persons")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Person[].class)
                .returnResult()
                .getResponseBody();
        assertThat(persons).isNotNull();
        assertThat(persons.length).isEqualTo(10);
    }

    @Test
    @Order(2)
    void testFindPersonById() {
        int id = 4;
        Person person = webTestClient.get()
                .uri("/persons/" + id)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Person.class)
                .returnResult()
                .getResponseBody();
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(id);
    }

}
