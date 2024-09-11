import DB.EmpByCompanyDbCheck;
import DB.EmployeeDbCheck;
import Methods.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class DBTests {
    private static String userToken;
    private static int companyId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com";
        userToken = Service.authenticateAndGetUserToken();
        companyId = Service.createCompanyAndGetId(userToken);
    }

    @Test
    @DisplayName("Добавление сотрудника и проверка его в базе по id")
    public void addEmployeeCheckDB() throws SQLException {
        int id = Service.addNewEmployee(userToken, companyId);
        boolean isEmployeeInDb = EmployeeDbCheck.isEmployeeInDatabase(id);
        assertTrue(isEmployeeInDb);

    }

    @Test
    @DisplayName("Смотрим в базе, что в компанию добавился сотрудник")
    public void checkEmployeeByCompany() throws SQLException {
        Service.addNewEmployee(userToken, companyId);
        boolean isEmployeeInCompany = EmpByCompanyDbCheck.companyCheck(companyId);
        assertTrue(isEmployeeInCompany);
    }

    @Test
    @DisplayName("Создаем компанию и проверяем, что она пустая")
    public void isCompanyEmpty() throws SQLException {
        Service.createCompanyAndGetId(userToken);
        boolean isEmployeeInCompany = EmpByCompanyDbCheck.companyCheck(companyId);
        assertFalse(isEmployeeInCompany);
    }
}
