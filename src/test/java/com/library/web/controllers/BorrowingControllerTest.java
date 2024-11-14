package com.library.web.controllers;

import com.library.AbstractIntegration;
import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.models.RegisterUserDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "/test-data.sql")
class BorrowingControllerTest extends AbstractIntegration {

    private String token;

    @BeforeEach
    void registerAndGetAccessToken() {
        RegisterUserDto registerUserDto = new RegisterUserDto(
                "test@user.com", "test", "test", "P@ssw0rd1000");

        AccessTokenRequestDto accessTokenRequestDto =
                new AccessTokenRequestDto("test@user.com", "P@ssw0rd1000");

        given()
                .contentType(ContentType.JSON)
                .body(registerUserDto)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(accessTokenRequestDto)
                .when()
                .post("/api/auth/access-token")
                .then()
                .statusCode(201)
                .extract()
                .response();

        token = response.jsonPath().getString("accessToken");
    }

    @Test
    void shouldBorrowBookSuccessfully() {
        Long bookId = 2L;
        Long patronId = 2L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnBookSuccessfully() {
        Long bookId = 2L;
        Long patronId = 1L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                .then()
                .statusCode(HttpStatus.OK.value());

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .put("/api/borrow/{bookId}/return/{patronId}", bookId, patronId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() {
        Long invalidBookId = 999L;
        Long patronId = 1L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", invalidBookId, patronId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Book Not Found"))
                .body("detail", is("Book with id: " + invalidBookId + " not found."));
    }

    @Test
    void shouldReturnBookNotAvailable() {
        Long bookId = 1L;
        Long patronId = 999L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{invalidPatronId}", bookId, patronId)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("status", is(HttpStatus.CONFLICT.value()));
    }

    @Test
    void shouldReturnNotFoundWhenPatronDoesNotExist() {
        Long bookId = 2L;
        Long invalidPatronId = 999L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{invalidPatronId}", bookId, invalidPatronId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Patron Not Found"))
                .body("detail", is("Patron with id: " + invalidPatronId + " not found."));
    }

    @Test
    void shouldReturnPatronAlreadyBorrowedTheBook() {
        Long bookId = 3L;
        Long patronId = 2L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("status", is(HttpStatus.FORBIDDEN.value()))
                .body("title", is("Patron Already Borrowed The Book"))
                .body("detail", is("Patron with id: " + patronId + " has borrowed book with id: " + bookId));
    }

    @Test
    void shouldReturnForbiddenWhenPatronNotActive() {
        Long bookId = 3L;
        Long patronId = 3L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/borrow/{bookId}/patron/{inactivePatronId}", bookId, patronId)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("status", is(HttpStatus.CONFLICT.value()))
                .body("title", is("Patron Not Allowed To Borrow"))
                .body("detail", is("Patron with id: " + patronId + " not allowed to borrow books."));
    }
}
