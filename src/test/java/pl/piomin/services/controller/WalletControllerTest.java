package pl.piomin.services.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.resttestclient.autoconfigure.RestTestClientBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.lang.reflect.Method;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class WalletControllerTest {

    @TestConfiguration
    static class TimeoutConfig {
        @Bean
        RestTestClientBuilderCustomizer longReadTimeoutCustomizer() {
            return builder -> {
                try {
                    ReactorClientHttpRequestFactory factory = new ReactorClientHttpRequestFactory();
                    factory.setReadTimeout(Duration.ofSeconds(120));
                    Class<?> cls = builder.getClass();
                    while (cls != null) {
                        try {
                            Method m = cls.getDeclaredMethod("setClientHttpRequestFactory",
                                    org.springframework.http.client.ClientHttpRequestFactory.class);
                            m.setAccessible(true);
                            m.invoke(builder, factory);
                            return;
                        } catch (NoSuchMethodException ex) {
                            cls = cls.getSuperclass();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }

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
