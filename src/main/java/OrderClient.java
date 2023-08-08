import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    public static final String ORDERS_URL = "/api/orders/";
    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpecification())
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }
}
