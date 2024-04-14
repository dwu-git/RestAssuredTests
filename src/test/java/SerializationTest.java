import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojosForSerializationTest.AddPlace;
import pojosForSerializationTest.Location;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializationTest {
    // Serialization: JSON -> POJO
    @Test
    public void serializationTest() {
        AddPlace addPlaceBody = new AddPlace();
        addPlaceBody.setLocation(new Location(-38.383494, 33.427362));
        addPlaceBody.setAccuracy(50);
        addPlaceBody.setName("Frontline house");
        addPlaceBody.setPhoneNumber("(+91) 983 893 3937");
        addPlaceBody.setAddress("29, side layout, cohen 09");
        addPlaceBody.setTypes(List.of("shoe park", "shop"));
        addPlaceBody.setWebsite("http://rahulshettyacademy.com");
        addPlaceBody.setLanguage("French-IN");

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        Response addPlaceResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(addPlaceBody)
                .when().post("maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200)
                .extract().response();

        String addPlaceResponseString = addPlaceResponse.asString();
        System.out.println(addPlaceResponseString);
    }
}
