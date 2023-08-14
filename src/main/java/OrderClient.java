import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    public static final String ORDERS_URL = "/api/orders/";
    public static final String INGREDIENTS_URL = "/api/ingredients";
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createWOAuth(Order order) {
        return given()
                .spec(getSpecification())
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createWAuth(String accessToken, Order order) {
        return given()
                .spec(getSpecification())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }
    @Step("Получение информации об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpecification())
                .when()
                .get(INGREDIENTS_URL)
                .then().log().all();
    }
    @Step("Получение данных о заказах конкретного пользователя")
    public ValidatableResponse getOrderDataUser(String accessToken) {
        return given()
                .spec(getSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_URL)
                .then().log().all();
    }
}
