import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojoForECommerceApiTest.LoginRequestCredentials;
import pojoForECommerceApiTest.LoginResponseCredentials;

import java.io.File;

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

        RequestSpecification createProductRequest = given().log().all().spec(createProductBaseRequest)
                .param("productName", "Laptop")
                .param("productAddedBy", userId)
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", 11500)
                .param("productDescription", "Dell")
                .param("productFor", "women")
                .multiPart("productImage", new File("src/main/java/files/CreateProductImage.png"));

        String createProductResponse = createProductRequest.when().post("api/ecom/product/add-product")
                .then().log().all().extract().response().asString();

        JsonPath js = new JsonPath(createProductResponse);
        productId = js.getString("productId");
    }

    @Test
    public void createOrderTest() {

    }
}
