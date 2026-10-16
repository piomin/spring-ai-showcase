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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureRestTestClient
class PersonControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    @Order(1)
    void testFindAllPersons() {
        Person[] persons = restTestClient.get()
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
        Person person = restTestClient.get()
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
