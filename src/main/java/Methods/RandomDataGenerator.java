package Methods;

import com.github.javafaker.Faker;

public class RandomDataGenerator {

    // Здесь хранятся методы для генерации данных.
    // Используются при добавлении сотрудников и компаний.

    private static final Faker FAKER = new Faker();

    public static String generateRandomFirstName() {
        return FAKER.name().firstName();
    }

    public static String generateRandomLastName() {
        return FAKER.name().lastName();
    }

    public static String generateRandomMiddleName() {
        return FAKER.name().name();
    }

    public static String generateRandomCompanyName() {
        return FAKER.company().name();
    }

    public static String generateRandomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String generateRandomPhone() {
        return FAKER.phoneNumber().cellPhone();
    }

    public static String generateRandomDescription() {
        return FAKER.elderScrolls().creature();
    }

    public static String generateRandomUrl() {
        return FAKER.internet().url();
    }

    public static String generateRandomDate() {
        return FAKER.date().birthday().toString();
    }

    public static boolean generateActivity() {
        return FAKER.bool().bool();
    }
}
