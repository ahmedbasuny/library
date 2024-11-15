package com.library.web.controllers;

import com.library.AbstractIntegration;
import com.library.common.authentication.models.AccessTokenRequestDto;
import com.library.common.authentication.models.RegisterUserDto;
import com.library.domain.book.models.CreateBookDto;
import com.library.domain.book.models.UpdateBookDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Sql(scripts = "/test-data.sql")
class BookControllerTest extends AbstractIntegration {

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
                .get("/api/books")
                .then()
                .statusCode(401);  // 401 Unauthorized
    }

    @Test
    void shouldReturnBooksWithPagination() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .when()
                .get("/api/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(4))
                .body("totalElements", is(4))
                .body("pageable.pageNumber", is(0))
                .body("totalPages", is(1))
                .body("first", is(true))
                .body("last", is(true))
                .body("empty", is(false));
    }

    @Test
    void ShouldReturnInvalidIsbnFormat() {
        CreateBookDto newBook = new CreateBookDto("New Book", "Author Name",
                2023, "1234567890123", "Science Fiction", 10);

        given().contentType(ContentType.JSON)
                .body(newBook)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/books")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("fieldErrors.isbn",
                        is("Invalid ISBN format. ISBN should be either ISBN-10 or ISBN-13."));
    }

    @Test
    void shouldAddBook() {
        CreateBookDto newBook = new CreateBookDto("New Book", "Author Name",
                2023, "9781245412457", "Science Fiction", 10);

        given().contentType(ContentType.JSON)
                .body(newBook)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/api/books")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("title", is("New Book"))
                .body("author", is("Author Name"))
                .body("publicationYear", is(2023))
                .body("isbn", is("9781245412457"))
                .body("genre", is("Science Fiction"))
                .body("copiesAvailable", is(10));
    }

    @Test
    void shouldGetBookById() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .log().all()
                .get("/api/books/{id}", 4)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(4))
                .body("title", is("Pride and Prejudice"))
                .body("author", is("Jane Austen"))
                .body("publicationYear", is(1813))
                .body("isbn", is("9780141040349"))
                .body("genre", is("Romance"))
                .body("copiesAvailable", is(2));
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() {
        long invalidBookId = 999L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .log().all()
                .get("/api/books/{id}", invalidBookId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Book Not Found"))
                .body("detail", is("Book with id: " + invalidBookId + " not found."));
    }

    @Test
    void shouldUpdateBook() {
        Long bookId = 2L;
        UpdateBookDto updateBookDto = new UpdateBookDto(
                "Updated Title", "Updated Author", 2022,
                "9780124273515", "Non-Fiction", 5);

        given().contentType(ContentType.JSON)
                .body(updateBookDto)
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/api/books/{id}", bookId)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(2))
                .body("title", is("Updated Title"))
                .body("author", is("Updated Author"))
                .body("publicationYear", is(2022))
                .body("isbn", is("9780124273515"))
                .body("genre", is("Non-Fiction"))
                .body("copiesAvailable", is(5));
    }

    @Test
    void shouldDeleteBookById() {
        Long bookId = 3L;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/books/{id}", bookId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/books/{id}", bookId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Book Not Found"))
                .body("detail", is("Book with id: " + bookId + " not found."));
    }
}