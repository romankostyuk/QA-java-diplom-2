import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreateTest {
    private User user;
    private String accessToken;
    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        user = User.generateRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredients.add(orderClient.getIngredients().extract().path("data[0]._id"));
        ingredients.add(orderClient.getIngredients().extract().path("data[1]._id"));
        order = new Order(ingredients);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
    @Test
    @DisplayName("Создание заказа: с авторизацией")
    public void createOrderWAuthTest() {
        accessToken = userClient.create(user).extract().path("accessToken");
        ValidatableResponse orderResponse = orderClient.createWAuth(accessToken, order);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа: без авторизации")
    public void createOrderWOAuthTest() {
        ValidatableResponse orderResponse = orderClient.createWOAuth(order);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWOIngredientsTest() {
        ingredients.clear();
        Order orderWrong = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createWOAuth(orderWrong);
        orderResponse
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингридиента")
    public void createOrderWWrongIngredientsTest() {
        ingredients.add("wrong_hash");
        Order orderWrong = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createWOAuth(orderWrong);
        orderResponse
                .statusCode(500);
    }
    @Test
    @DisplayName("Получить список заказов с авторизацией")
    public void getDataOrderWithAuthTest() {
        accessToken = userClient.create(user).extract().path("accessToken");
        ValidatableResponse orderResponse = orderClient.getOrderDataUser(accessToken);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Получить список заказов без авторизации")
    public void getDataOrderWOAuthTest() {
        accessToken = "";
        ValidatableResponse orderResponse = orderClient.getOrderDataUser(accessToken);
        orderResponse
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }
}
