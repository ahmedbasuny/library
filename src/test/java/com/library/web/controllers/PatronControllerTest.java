package com.library.web.controllers;

import com.library.AbstractIntegration;
import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.models.RegisterUserDto;
import com.library.common.enums.PatronStatus;
import com.library.domain.patron.models.CreatePatronDto;
import com.library.domain.patron.models.UpdatePatronDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "/test-data.sql")
public class PatronControllerTest extends AbstractIntegration {

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
                .statusCode(201)
                .extract()
                .response();

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
    void shouldReturnUnauthorizedWithoutToken() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/patrons")
                .then()
                .statusCode(401);  // 401 Unauthorized
    }

    @Test
    void shouldReturnPatronsWithPagination() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .when()
                .get("/api/patrons")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(3))
                .body("totalElements", is(3))
                .body("pageable.pageNumber", is(0))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("empty", is(false));
    }

    @Test
    void shouldAddPatron() {
        CreatePatronDto newPatron = new CreatePatronDto(
                "John Doe", "123-456-7890", "johndoe@example.com",
                "Alex", LocalDate.now(), PatronStatus.ACTIVE);

        given().contentType(ContentType.JSON)
                .body(newPatron)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/patrons")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", is("John Doe"))
                .body("email", is("johndoe@example.com"))
                .body("mobile", is("123-456-7890"))
                .body("status", is("ACTIVE"));
    }

    @Test
    void shouldGetPatronById() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patrons/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(1))
                .body("name", is("Ahmed Basuny"))
                .body("email", is("ahmedbasuny13@gmail.com"))
                .body("mobile", is("01276063525"))
                .body("status", is("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenPatronDoesNotExist() {
        long invalidPatronId = 999L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patrons/{id}", invalidPatronId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Patron Not Found"))
                .body("detail", is("Patron with id: " + invalidPatronId + " not found."));
    }

    @Test
    void shouldUpdatePatron() {
        Long patronId = 2L;
        UpdatePatronDto updatePatronDto = new UpdatePatronDto(
                "Jane Doe", "321-654-0987", "janedoe@example.com",
                "Alex", LocalDate.now(), PatronStatus.INACTIVE);

        given().contentType(ContentType.JSON)
                .body(updatePatronDto)
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/api/patrons/{id}", patronId)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(2))
                .body("name", is("Jane Doe"))
                .body("email", is("janedoe@example.com"))
                .body("mobile", is("321-654-0987"))
                .body("status", is("INACTIVE"));
    }

    @Test
    void shouldDeletePatronById() {
        Long patronId = 3L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/patrons/{id}", patronId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patrons/{id}", patronId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Patron Not Found"))
                .body("detail", is("Patron with id: " + patronId + " not found."));
    }
}
