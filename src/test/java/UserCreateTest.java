import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class UserCreateTest {
    private User user;
    private String accessToken;
    private UserClient userClient;

    @Before
    public void setup() {
        userClient = new UserClient();
        user = User.generateRandomUser();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
    @Test
    @DisplayName("можно. создать уникального пользователя")
    public void userCreatedTest() {
        ValidatableResponse registrationResponse = userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken").toString();
        registrationResponse
                .statusCode(200)
                .assertThat().body("success", is(true));
    }
    @Test
    @DisplayName("нельзя. создать пользователя, который уже зарегистрирован")
    public void duplicateUserCannotBeCreatedTest() {
        userClient.create(user);
        ValidatableResponse registrationResponse = userClient.create(user);
        registrationResponse
                .statusCode(403)
                .assertThat().body("success", is(false));
    }
    //создать пользователя и не заполнить одно из обязательных полей. - я знаю что это можно параметризовать, но не хочется нарушать структуру
    @Test
    @DisplayName("нельзя. создать пользователя и не заполнить email")
    public void userWoEmailCannotBeCreatedTest() {
        user.setEmail(null);
        ValidatableResponse registrationResponse = userClient.create(user);
        registrationResponse
                .statusCode(403)
                .assertThat().body("success", is(false));
    }
    @Test
    @DisplayName("нельзя. создать пользователя и не заполнить name")
    public void userWoNameCannotBeCreatedTest() {
        user.setName(null);
        ValidatableResponse registrationResponse = userClient.create(user);
        registrationResponse
                .statusCode(403)
                .assertThat().body("success", is(false));
    }
    @Test
    @DisplayName("нельзя. создать пользователя и не заполнить password")
    public void userWoPasswordCannotBeCreatedTest() {
        user.setPassword(null);
        ValidatableResponse registrationResponse = userClient.create(user);
        registrationResponse
                .statusCode(403)
                .assertThat().body("success", is(false));
    }
}