import Methods.Service;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessTests {

    // БИЗНЕС ТЕСТЫ.
    // Класс Service содержит в себе методы по отправке ключевых запросов.
    // Дисклеймер. Возможно, некоторые тесты следовало бы разместить в контрактные.


    private static String userToken;
    private static int companyId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com";
        userToken = Service.authenticateAndGetUserToken();
        companyId = Service.createCompanyAndGetId(userToken);
    }

    // Тест провален. Email не передается при добавлении сотрудника и равен null
    @Test
    @DisplayName("Добавление сотрудника и проверка полей")
    public void addNewEmployee() {
        int id = Service.addNewEmployee(userToken, companyId);
        Response response = Service.getEmpInfo(id);
        assertNotNull(response.jsonPath().getString("firstName"));
        assertNotNull(response.jsonPath().getString("lastName"));
        assertNotNull(response.jsonPath().getString("middleName"));
        assertNotNull(response.jsonPath().getString("email"));
        assertNotNull(response.jsonPath().getString("url"));
        assertNotNull(response.jsonPath().getString("phone"));
        assertNotNull(response.jsonPath().getString("birthdate"));
        assertNotNull(response.jsonPath().getString("isActive"));
    }


    @Test
    @DisplayName("Добавить сотрудников в компанию и запросить список")
    public void getEmployeeList() {

        // Добавление сотрудников
        int emp1 = Service.addNewEmployee(userToken, companyId);
        int emp2 = Service.addNewEmployee(userToken, companyId);

        Response response = Service.getEmployeesByCompanyId(companyId);

        // Проверка наличия добавленных сотрудников, количества сотрудников в списке
        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("id");

        assertFalse(ids.isEmpty());
        assertEquals(2, ids.size());
        assertTrue(ids.contains(emp1));
        assertTrue(ids.contains(emp2));

        System.out.println("Список сотрудников компании " + companyId + ": " + response.getBody().asString());
    }

    // Тест провален. У пользователя не сменился номер телефона
    @Test
    @DisplayName("Добавляем сотрудника и редактируем его данные. Проверяем изменения")
    public void editEmployee() {

        // Добавляем сотрудника
        int id = Service.addNewEmployee(userToken, companyId);

        // Вытаскиваем данные для сравнения
        Response response = Service.getEmpInfo(id);
        String lastName = response.jsonPath().getString("lastName");
        String email = response.jsonPath().getString("email");
        String phone = response.jsonPath().getString("phone");
        String url = response.jsonPath().getString("url");

        // Редактируем данные
        Service.editEmployee(userToken, id);
        Response response1 = Service.getEmpInfo(id);

        // Вытаскиваем измененные данные для сравнения
        String editedLastName = response1.jsonPath().getString("lastName");
        String editedEmail = response1.jsonPath().getString("email");
        String editedPhone = response1.jsonPath().getString("phone");
        String editedUrl = response1.jsonPath().getString("url");

        // Проверяем отличие данных
        assertNotEquals(editedLastName, lastName);
        assertNotEquals(editedEmail, email);
        assertNotEquals(editedPhone, phone);
        assertNotEquals(editedUrl, url);

    }


// Негативные тесты

    @Test
    @DisplayName("Запрашиваем список сотрудников пустой компании")
    public void emptyCompanyList() {

        Response response = Service.getEmployeesByCompanyId(companyId);

        // Проверка отсутствия сотрудников, количества сотрудников в списке
        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("id");

        assertTrue(ids.isEmpty());

        System.out.println("Список сотрудников компании " + companyId + ": " + response.getBody().asString());
    }

    // При добавлении сотрудника не отправляем никаких данных
    @Test
    @DisplayName("Добавление нового сотрудника без данных")
    public void noFieldsSent() {
        Response response = Service.addNewEmpNoDataSent(userToken);
        assertEquals(500, response.statusCode());
    }

    // Тест провален - на запрос несуществующего пользователя приходит статус код 200
    @Test
    @DisplayName("Запросить несуществующего сотрудника")
    public void getUnexistingEmp() {
        int unexistingID = 1985571221;
        Response response = Service.getEmpInfo(unexistingID);
        assertFalse(response.statusCode() == 200 || response.statusCode() == 201);
    }

    // Тест провален - на запрос из несуществующей компании приходит статус код 200
    @Test
    @DisplayName("Запросить список из несуществующей компании")
    public void getListFromUnexistingCompany() {
        int unexsitingCompanyId = 1535154684;
        Response response = Service.getEmployeesByCompanyId(unexsitingCompanyId);
        assertFalse(response.statusCode() == 200 || response.statusCode() == 201);
    }

    @Test
    @DisplayName("Редактировать данные несуществующего сотрудника")
    public void editUnexistingEmp() {
        int unexistingEmpID = 19622554;
        Response response = Service.editEmployee(userToken, unexistingEmpID);
        assertFalse(response.statusCode() == 200 || response.statusCode() == 201);
        System.out.println("Вернулся статус код " + response.statusCode());
    }

    // Тест провален. Редактирование без отправки данных дает код 200
    @Test
    @DisplayName("Редактировать данные без отправки полей")
    public void editWithoutDataSent() {
        int id = Service.addNewEmployee(userToken, companyId);
        Response response = Service.editEmployeeWithNoData(userToken, id);
        System.out.println("Вернулся статус код " + response.statusCode());
        assertFalse(response.statusCode() == 200 || response.statusCode() == 201);

    }

    // Более расширенная версия предыдущего теста
    @Test
    @DisplayName("Редактировать данные без отправки полей (расширенный)")
    public void editEmployeeNoData() {

        // Добавляем сотрудника
        int id = Service.addNewEmployee(userToken, companyId);

        // Вытаскиваем данные для сравнения
        Response response = Service.getEmpInfo(id);
        String lastName = response.jsonPath().getString("lastName");
        String email = response.jsonPath().getString("email");
        String phone = response.jsonPath().getString("phone");
        String url = response.jsonPath().getString("url");

        // Редактируем данные
        Service.editEmployeeWithNoData(userToken, id);
        Response response1 = Service.getEmpInfo(id);

        // Вытаскиваем данные после PATCH запроса
        String editedLastName = response1.jsonPath().getString("lastName");
        String editedEmail = response1.jsonPath().getString("email");
        String editedPhone = response1.jsonPath().getString("phone");
        String editedUrl = response1.jsonPath().getString("url");

        // Проверяем, что данные не пострадали от некорректного запроса
        assertEquals(editedLastName, lastName);
        assertEquals(editedEmail, email);
        assertEquals(editedPhone, phone);
        assertEquals(editedUrl, url);

    }

}
