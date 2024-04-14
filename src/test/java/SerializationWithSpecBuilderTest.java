import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pojosForSerializationTest.AddPlace;
import pojosForSerializationTest.Location;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializationWithSpecBuilderTest {
    // Serialization: JSON -> POJO
    @Test
    public void serializationTest() {
        AddPlace bodyForAddPlaceApi = getBodyForAddPlaceApi();

        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("http://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();

        ResponseSpecification responseSpecificationForAddPlaceApi = new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RequestSpecification requestSpecificationForAddPlaceApi = given().log().all()
                .spec(requestSpecification)
                .body(bodyForAddPlaceApi);

        Response addPlaceResponse = requestSpecificationForAddPlaceApi
                .when().post("maps/api/place/add/json")
                .then().log().all().spec(responseSpecificationForAddPlaceApi)
                .extract().response();

        String addPlaceResponseString = addPlaceResponse.asString();
        System.out.println(addPlaceResponseString);
    }

    private static AddPlace getBodyForAddPlaceApi() {
        AddPlace bodyForAddPlaceApi = new AddPlace();
        bodyForAddPlaceApi.setLocation(new Location(-38.383494, 33.427362));
        bodyForAddPlaceApi.setAccuracy(50);
        bodyForAddPlaceApi.setName("Frontline house");
        bodyForAddPlaceApi.setPhoneNumber("(+91) 983 893 3937");
        bodyForAddPlaceApi.setAddress("29, side layout, cohen 09");
        bodyForAddPlaceApi.setTypes(List.of("shoe park", "shop"));
        bodyForAddPlaceApi.setWebsite("http://rahulshettyacademy.com");
        bodyForAddPlaceApi.setLanguage("French-IN");
        return bodyForAddPlaceApi;
    }
}
