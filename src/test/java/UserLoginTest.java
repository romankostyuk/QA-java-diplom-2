import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class UserLoginTest {
    private User user;
    private String accessToken;
    private UserClient userClient;


    @Before
    public void setup() {
        user = User.generateRandomUser();
        userClient = new UserClient();
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
    @Test
    @DisplayName("Проверка логина под существующим пользователем")
    public void userLoginTest() {
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");
        loginResponse
                .statusCode(200)
                .assertThat()
                .body("accessToken", is(notNullValue()));

    }
    @Test
    @DisplayName("Проверка логина несуществующим пользователем")
    public void userCannotBeLoginWWrongEmailAndPasswordTest() {
        userClient.create(user);
        user.setEmail("WrongEmail");
        user.setPassword("WrongPassword");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        loginResponse
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }
}