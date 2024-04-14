package org.example;

import utils.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class JsonParsing {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(Payload.coursePrice());
        int numberOfCourses = js.getInt("courses.size()");
        System.out.println(numberOfCourses);

        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(purchaseAmount);

        String firstCourseTitle = js.get("courses[0].title");
        System.out.println(firstCourseTitle);

        for (int i = 0; i < numberOfCourses; i++) {
            String courseTitle = js.get("courses[" + i + "].title");
            int coursePrice = js.get("courses[" + i + "].price");
            System.out.println(courseTitle);
            System.out.println(coursePrice);
        }

        System.out.println("Print a number of copies in RPA course");
        for (int i = 0; i < numberOfCourses; i++) {
            String getCourseTitle = js.get("courses[" + i + "].title");
            if (getCourseTitle.equals("RPA")) {
                System.out.println(js.getInt("courses[" + i + "].copies"));
                break;
            }
        }

        System.out.println("Verify the purchase amount equals the sum of all course prices");
        int sumOfAllCoursePrices = 0;

        for (int i = 0; i < numberOfCourses; i++)
            sumOfAllCoursePrices += js.getInt("courses[" + i + "].price");

        System.out.println(purchaseAmount);
        System.out.println(sumOfAllCoursePrices);
        Assert.assertEquals(sumOfAllCoursePrices, purchaseAmount);
    }
}
