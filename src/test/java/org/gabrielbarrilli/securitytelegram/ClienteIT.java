package org.gabrielbarrilli.securitytelegram;

import org.gabrielbarrilli.securitytelegram.web.dto.ClienteCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

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
}
