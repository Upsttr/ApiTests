import Methods.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContractTests {

    // КОНТРАКТНЫЕ ТЕСТЫ

    private static String userToken;
    private static int companyId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com";
        userToken = Service.authenticateAndGetUserToken();
        companyId = Service.createCompanyAndGetId(userToken);
    }

    @Test
    @DisplayName("Авторизация и получение токена")
    public void authCheck() {
        Service.authenticateAndGetUserToken();
        System.out.println("Администратор авторизован");
    }

    @Test
    @DisplayName("Запрашиваем список сотрудников")
    public void employeeListCheck() {
        Service.getEmployeesByCompanyId(companyId);
        System.out.println("Получен список сотрудников компании: " + companyId);
    }

    @Test
    @DisplayName("Добавление сотрудника")
    public void addEmployeeCheck() {
        int id = Service.addNewEmployee(userToken, companyId);
        System.out.println("Добавлен сотрудник " + id);
    }

    @Test
    @DisplayName("Создание компании")
    public void addCompanyCheck() {
        int companyId = Service.createCompanyAndGetId(userToken);
        System.out.println("Создана компания " + companyId);
    }

    @Test
    @DisplayName("Редактирование данных сотрудника")
    public void employeeEditCheck() {
        int id = Service.addNewEmployee(userToken, companyId);
        Response response = Service.editEmployee(userToken, id);
        assertEquals(200, response.statusCode());
        System.out.println("Данные сотрудника " + id + " были изменены");
    }

    @Test
    @DisplayName("Получение данных сотрудника")
    public void getEmployeeInfo() {
        int id = Service.addNewEmployee(userToken, companyId);
        Service.getEmpInfo(id);
        System.out.println("Получены данные о сотруднике " + id);
    }
}
