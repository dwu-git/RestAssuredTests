import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojoForECommerceApiTest.LoginRequestCredentials;
import pojoForECommerceApiTest.LoginResponseCredentials;
import pojoForECommerceApiTest.OrdersDetails;
import pojoForECommerceApiTest.Orders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ECommerceApiTest {
    private String token;
    private String userId;
    private String productId;

    @BeforeTest
    public void loginTest() {
        RequestSpecification request = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
        LoginRequestCredentials loginRequestCredentials = new LoginRequestCredentials("restassured@rwea.com", "FHgtXhggjvu453%$@$");
        RequestSpecification loginRequest = given().log().all().spec(request).body(loginRequestCredentials);
        LoginResponseCredentials loginResponse = loginRequest.when().post("api/ecom/auth/login")
                 .then().log().all().extract().response().as(LoginResponseCredentials.class);

        token = loginResponse.getToken();
        userId = loginResponse.getUserId();
    }

    @Test
    public void createProductTest() {
        RequestSpecification createProductBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token).build();

        // form-data request with attachment
        RequestSpecification createProductRequest = given().log().all().spec(createProductBaseRequest)
                .param("productName", "Laptop")
                .param("productAddedBy", userId)
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", 11500)
                .param("productDescription", "Dell")
                .param("productFor", "women")
                .multiPart("productImage", new File("src/main/java/files/CreateProductImage.png")); // attachment

        String createProductResponse = createProductRequest.when().post("api/ecom/product/add-product")
                .then().log().all().extract().response().asString();

        JsonPath js = new JsonPath(createProductResponse);
        productId = js.getString("productId");
    }

    @Test(dependsOnMethods = { "createProductTest" })
    public void createOrderTest() {
        RequestSpecification createOrderBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token).setContentType(ContentType.JSON).build();

        OrdersDetails ordersDetails = new OrdersDetails("India", productId);
        List<OrdersDetails> ordersDetailsList = new ArrayList<>();
        ordersDetailsList.add(ordersDetails);

        Orders orders = new Orders();
        orders.setOrders(ordersDetailsList);

        // relaxedHTTPSValidation() -> This means that you'll trust all hosts regardless if the SSL certificate is invalid (sometimes with proxy needed)
        RequestSpecification createOrderRequest = given().relaxedHTTPSValidation().log().all().spec(createOrderBaseRequest).body(orders);
        String createOrderResponse = createOrderRequest.when().post("api/ecom/order/create-order")
                .then().log().all()
                .extract().response().asString();

        System.out.println(createOrderResponse);
    }

    @Test(dependsOnMethods = { "createProductTest", "createOrderTest" })
    public void deleteProductTest() {
        RequestSpecification deleteProductBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token).build();

        RequestSpecification deleteProductRequest = given().log().all().spec(deleteProductBaseRequest).pathParam("productId", productId);
        String deleteProductResponse = deleteProductRequest.when().delete("https://rahulshettyacademy.com/api/ecom/product/delete-product/{productId}")
                .then().log().all()
                .extract().response().asString();

        JsonPath js = new JsonPath(deleteProductResponse);
        Assert.assertEquals(js.getString("message"), "Product Deleted Successfully");
    }
}
