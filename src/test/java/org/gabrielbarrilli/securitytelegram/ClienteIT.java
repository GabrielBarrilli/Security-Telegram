package org.gabrielbarrilli.securitytelegram;

import org.gabrielbarrilli.securitytelegram.web.dto.ClienteCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteResponseDto;
import org.gabrielbarrilli.securitytelegram.web.dto.PageableDto;
import org.gabrielbarrilli.securitytelegram.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/cliente-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/cliente-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void criarCliente_comDadosValidos_retornarClienteComStatus201() {
        ClienteResponseDto responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "78833743039"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");
        assertThat(responseBody.getCpf()).isEqualTo("78833743039");
    }

    @Test
    public void criarCliente_comCpfJaCadastrado_retornarErrorMessage409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "38318819012"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void criarCliente_comDadosInvalidos_retornarErrorMessage422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("toby", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("toby", "788.337.430-39"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_comUsuarioNaoPermitido_retornarErrorMessage403() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "78833743039"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void acharCliente_comIdValido_retornarClienteComStatus200() {
        ClienteResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Lara Leticia");
        assertThat(responseBody.getCpf()).isEqualTo("02836171014");
    }

    @Test
    public void acharCliente_comIdInexistentePeloAdmin_retornarErrorMessage404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes/0")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void acharCliente_comIdExistentePeloCliente_retornarErrorMessage403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lara@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarClientes_comPaginacaoPeloAdmin_retornarClientesComStatus200() {
        PageableDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(2);
        assertThat(responseBody.getNumber()).isEqualTo(0);
        assertThat(responseBody.getTotalPages()).isEqualTo(1);

        responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(1);
        assertThat(responseBody.getNumber()).isEqualTo(1);
        assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void buscarClientes_comPaginacaoPeloCliente_retornarErrorMessageComStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lara@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarClientes_comDadosDoTokenDeCliente_retornarClienteComStatus200() {
        ClienteResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes/detalhes")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lara@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
    }

    @Test
    public void buscarClientes_comDadosDoTokenDeAdmin_retornarErrorMessageComStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clientes/detalhes")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "admin@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
