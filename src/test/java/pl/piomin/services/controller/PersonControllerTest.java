package pl.piomin.services.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import pl.piomin.services.model.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureRestTestClient
class PersonControllerTest {

    @Autowired
    private RestTestClient webTestClient;

    @Test
    @Order(1)
    void testFindAllPersons() {
        webTestClient.get()
            .uri("/persons")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Person[].class)
                .value(people -> assertEquals(10, people.length));
    }

    @Test
    @Order(2)
    void testFindPersonById() {
        int id = 4;
        webTestClient.get()
            .uri("/persons/" + id)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Person.class)
            .value(person -> {
                assertThat(person).isNotNull();
                assertThat(person.getId()).isEqualTo(id);
            });
    }

}
