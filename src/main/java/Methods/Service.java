package Methods;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class Service {

    // Здесь хранятся методы, которые упрощают тесты.

    static String baseUri = "https://x-clients-be.onrender.com";

    public static String authenticateAndGetUserToken() {
        RestAssured.baseURI = baseUri;

        String requestBody = "{"
                             + "\"username\": \"raphael\","
                             + "\"password\": \"cool-but-crude\""
                             + "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(baseURI + "/auth/login")
                .then()
                .statusCode(201)
                .extract().response();

        // Извлекаем токен из ответа
        return response.jsonPath().getString("userToken");
    }

    public static int createCompanyAndGetId(String userToken) {
        RestAssured.baseURI = baseUri;
        String companyName = RandomDataGenerator.generateRandomCompanyName();
        String description = RandomDataGenerator.generateRandomDescription();

        String requestBody = "{"
                             + "\"name\": \"" + companyName + "\","
                             + "\"description\": \"" + description + "\""
                             + "}";

        Response response = given()
                .header("x-client-token", userToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/company")
                .then()
                .statusCode(201)
                .extract().response();

        // Извлекаем ID компании из ответа
        return response.jsonPath().getInt("id");


    }

    public static int addNewEmployee(String userToken, int companyId) {
        RestAssured.baseURI = baseUri;

        String firstName = RandomDataGenerator.generateRandomFirstName();
        String lastName = RandomDataGenerator.generateRandomLastName();
        String middleName = RandomDataGenerator.generateRandomMiddleName();
        String email = RandomDataGenerator.generateRandomEmail();
        String url = RandomDataGenerator.generateRandomUrl();
        String phone = RandomDataGenerator.generateRandomPhone();
        String birthdate = RandomDataGenerator.generateRandomDate();
        boolean isActive = RandomDataGenerator.generateActivity();

        String requestBody = "{"
                             + "\"firstName\": \"" + firstName + "\","
                             + "\"lastName\": \"" + lastName + "\","
                             + "\"middleName\": \"" + middleName + "\","
                             + "\"companyId\": " + companyId + ","
                             + "\"email\": \"" + email + "\","
                             + "\"url\": \"" + url + "\","
                             + "\"phone\": \"" + phone + "\","
                             + "\"birthdate\": \"" + birthdate + "\","
                             + "\"isActive\": " + isActive
                             + "}";

        Response response = given()
                .header("x-client-token", userToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/employee")
                .then()
                .statusCode(201)
                .extract().response();


        return response.jsonPath().getInt("id");
    }


    public static Response addNewEmpNoDataSent(String userToken) {
        RestAssured.baseURI = baseUri;

        return given()
                .header("x-client-token", userToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/employee")
                .then()
                .extract().response();


    }

    public static Response getEmployeesByCompanyId(int companyId) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get("/employee?company=" + companyId)
                .then()
                .statusCode(200)
                .body("", notNullValue())
                .extract()
                .response();
    }

    public static Response getEmpInfo(int id) {
        RestAssured.baseURI = baseUri;

        return given()
                .accept(ContentType.JSON)
                .when()
                .get("/employee/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public static Response editEmployee(String userToken, int userId) {
        RestAssured.baseURI = baseUri;

        String lastName = RandomDataGenerator.generateRandomLastName();
        String email = RandomDataGenerator.generateRandomEmail();
        String url = RandomDataGenerator.generateRandomUrl();
        String phone = RandomDataGenerator.generateRandomPhone();
        boolean isActive = RandomDataGenerator.generateActivity();


        String requestBody = "{"
                             + "\"lastName\": \"" + lastName + "\","
                             + "\"email\": \"" + email + "\","
                             + "\"url\": \"" + url + "\","
                             + "\"phone\": \"" + phone + "\","
                             + "\"isActive\": " + isActive
                             + "}";


        return given()
                .header("x-client-token", userToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/employee/" + userId)
                .then()
                .extract().response();


    }

    public static Response editEmployeeWithNoData(String userToken, int userId) {
        RestAssured.baseURI = baseUri;

        String requestBody = "{"
                             + "}";

        return given()
                .header("x-client-token", userToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/employee/" + userId)
                .then()
                .extract().response();


    }
}
