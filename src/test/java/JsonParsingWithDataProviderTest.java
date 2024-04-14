import utils.Payload;
import utils.ReusableMethods;
import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class JsonParsingWithDataProviderTest {
    @Test(dataProvider = "Isbn&Aisle")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        String addBookResponse = given().log().all().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when().post("/Library/Addbook.php")
                .then().assertThat().log().all().statusCode(200)
                .extract().response().asString();

        String id = ReusableMethods.getFieldValueInResponse(addBookResponse, "ID");
        System.out.println(id);
    }

    @DataProvider(name = "Isbn&Aisle")
    public static Object[][] getIsbnAndAisle() {
        return new Object[][] { {"isbn_ew111rwe", "aisle_wr111ew"}, {"isbn_ewr2222we", "aisle_w2222rew"}, {"isbn_ewrrs3333rwe", "aisle_we3333rew"} };
    }
}
