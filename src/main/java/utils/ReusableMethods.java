package utils;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
    public static String getFieldValueInResponse(String response, String fieldToParse) {
        JsonPath js = new JsonPath(response);
        return js.getString(fieldToParse);
    }
}
