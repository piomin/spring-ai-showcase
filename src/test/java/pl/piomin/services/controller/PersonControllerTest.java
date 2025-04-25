package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import pl.piomin.services.model.Person;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFindAllPersons() {
        ResponseEntity<Person[]> response = restTemplate.getForEntity("/persons", Person[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).length).isEqualTo(10);
    }

    @Test
    void testFindPersonById() {
        Long id = 4L;
        ResponseEntity<Person> byIdResponse = restTemplate.getForEntity("/persons/" + id, Person.class);
        assertThat(byIdResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(byIdResponse.getBody()).isNotNull();
        assertThat(byIdResponse.getBody().getId()).isEqualTo(id);
    }

}
