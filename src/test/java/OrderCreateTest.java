import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreateTest {
    private User user;
    private String accessToken;
    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = User.generateRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        List<String> ingredients = Arrays.asList("60d3b41abdacab0026a733c6","609646e4dc916e00276b2870");
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
        ValidatableResponse orderResponse = orderClient.create(order);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
}
