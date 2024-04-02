package org.gabrielbarrilli.securitytelegram;

import org.gabrielbarrilli.securitytelegram.web.dto.VagaCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.VagaResponseDto;
import org.gabrielbarrilli.securitytelegram.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vaga-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vaga-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void criarVaga_comDadosValidos_retornarLocationStatus201() {
        webTestClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-05", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVaga_comDadosJaExistentes_retornarErrorMessageComStatus409() {
        webTestClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-01", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath(("status")).isEqualTo(409)
                .jsonPath(("method")).isEqualTo("POST")
                .jsonPath(("path")).isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVaga_comDadosInvalidos_retornarErrorMessageComStatus422() {
        webTestClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .bodyValue(new VagaCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath(("status")).isEqualTo(422)
                .jsonPath(("method")).isEqualTo("POST")
                .jsonPath(("path")).isEqualTo("/api/v1/vagas");

        webTestClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-501", "DESOCUPADO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath(("status")).isEqualTo(422)
                .jsonPath(("method")).isEqualTo("POST")
                .jsonPath(("path")).isEqualTo("/api/v1/vagas");
    }

    @Test
    public void acharVaga_comDadosValidos_retornarVagaComStatus200() {
        VagaResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/vagas/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VagaResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(10);
        assertThat(responseBody.getCodigo()).isEqualTo("A-01");
        assertThat(responseBody.getStatus()).isEqualTo("LIVRE");

    }

    @Test
    public void acharVaga_comDadosInvalidos_retornarErrorMessageComStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/vagas/A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
    }
}
