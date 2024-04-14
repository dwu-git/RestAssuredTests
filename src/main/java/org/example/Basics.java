package org.example;

import utils.Payload;
import utils.ReusableMethods;
import io.restassured.RestAssured;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Add Place
        // Body from Payload class
        String postPlaceResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.addPlace())
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", equalTo("Apache/2.4.52 (Ubuntu)"))
                .extract().response().asString();

        System.out.println(postPlaceResponse);

        String placeId = ReusableMethods.getFieldValueInResponse(postPlaceResponse, "place_id");
        System.out.println(placeId);

        // Add Place
        // Body from Json file
        String postPlaceResponse2 = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Path.of("src/main/java/files/AddPlace.json")))) // Also convert byte[] -> String (for readability in Console)
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", equalTo("Apache/2.4.52 (Ubuntu)"))
                .extract().response().asString();

        System.out.println(postPlaceResponse2);

        String placeId2 = ReusableMethods.getFieldValueInResponse(postPlaceResponse, "place_id");
        System.out.println(placeId2);

        // Update Place
        String newAddress = "70 winter walk, USA";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{" +
                        "\"place_id\":\"" + placeId + "\"," +
                        "\"address\":\"" + newAddress + "\"," +
                        "\"key\":\"qaclick123\"" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        // Get Place
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        String actualAddress = ReusableMethods.getFieldValueInResponse(getPlaceResponse, "address");
        Assert.assertEquals(actualAddress, newAddress);

        System.out.println(actualAddress);
    }
}
