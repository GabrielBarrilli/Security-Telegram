package org.gabrielbarrilli.securitytelegram;

import org.gabrielbarrilli.securitytelegram.web.dto.UsuarioCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.UsuarioResponseDto;
import org.gabrielbarrilli.securitytelegram.web.dto.UsuarioSenhaDto;
import org.gabrielbarrilli.securitytelegram.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuario-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_comUsernameEPasswordValidos_retornarUsuarioCriadoComStatus201() {
        UsuarioResponseDto responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_comUsernameOuPasswordInvalidos_retornarErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_comUsernameEPasswordValidos_retornarUsuarioCriadoComStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lara@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void buscarUsuario_comIdExistente_retornarUsuarioComStatus200() {
        UsuarioResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/getById/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lara@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(100);
        assertThat(responseBody.getUsername()).isEqualTo("lara@email.com");
        assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/getById/101")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lara@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(101);
        assertThat(responseBody.getUsername()).isEqualTo("laral@email.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

        responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/getById/101")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "laral@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(101);
        assertThat(responseBody.getUsername()).isEqualTo("lara@email.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void buscarUsuario_comIdInexistente_retornarUsuarioComStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/getById/0")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void editarSenha_comDadosValidos_retornarStatus204() {
        webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "654321", "654321"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editarSenha_comIdInexistente_retornarErrorMessageComStatus404() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "654321", "654321"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void editarSenha_comCamposInvalidos_retornarStatus422() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "654321", "654321"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345", "65432", "65432"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("1234567", "6543210", "6543210"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void editarSenha_comSenhasInvalidas_retornarStatus400() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "654321", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/updatePassword/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("654321", "654321", "654321"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);

    }
}





