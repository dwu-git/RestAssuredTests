import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojosForOAuthDeserializationTest.Api;
import pojosForOAuthDeserializationTest.GetCourse;
import pojosForOAuthDeserializationTest.WebAutomation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OAuthDeserializationTest {
    private final String[] courseTitles = { "Selenium Webdriver Java", "Cypress", "Protractor" };
    // form-data (key, value)
    @Test
    public void oAuthTest() {
        String getAccessTokenResponse = given()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "true")
                .when().log().all()
                .post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();

        JsonPath js = new JsonPath(getAccessTokenResponse);
        String accessToken = js.getString("access_token");
        System.out.println(accessToken);




        // Deserialization: JSON -> POJO
        GetCourse getCourseDetailsResponse = given()
                .queryParam("access_token", accessToken)
                .when().log().all()
                .get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class);

        List<Api> apiCourses = getCourseDetailsResponse.getCourses().getApi();
        for (var apiCourse : apiCourses) {
            if (apiCourse.getCourseTitle().equals("SoapUI Webservices testing")) {
                System.out.println(apiCourse.getCourseTitle());
                System.out.println(apiCourse.getPrice());
                break;
            }
        }

        ArrayList<String> actualCourseTitles = new ArrayList<>();
        List<String> expectedCourseTitles = Arrays.asList(courseTitles);

        List<WebAutomation> webAutomationCourses = getCourseDetailsResponse.getCourses().getWebAutomation();
        for (var webAutomationCourse : webAutomationCourses)
            actualCourseTitles.add(webAutomationCourse.getCourseTitle());

        Assert.assertEquals(actualCourseTitles, expectedCourseTitles);
    }
}
