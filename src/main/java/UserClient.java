import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class UserClient extends RestClient{
    public static final String USER_URL = "/api/auth/";
    @Step("Регистрация пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpecification())
                .when()
                .body(user).log().all()
                .post(USER_URL + "register").then().log().all();
    }
    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getSpecification())
                .when()
                .body(userCredentials)
                .post(USER_URL + "login").then().log().all();
    }
    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpecification())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_URL + "user").then().log().all();

    }
    @Step("Изменения  пользователя")
    public ValidatableResponse edit(User user, String accessToken) {
        return given()
                .spec(getSpecification())
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch(USER_URL + "user").then().log().all();
    }
}
