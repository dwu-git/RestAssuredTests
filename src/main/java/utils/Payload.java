package utils;

public class Payload {
    public static String addPlace() {
        return """
                {
                    "location": {
                        "lat": -38.383494,
                        "lng": 33.427362
                    },
                    "accuracy": 50,
                    "name": "Frontline house",
                    "phone_number": "(+91) 983 893 3937",
                    "address": "29, side layout, cohen 09",
                    "types": [
                        "shoe park",
                        "shop"
                    ],
                    "website": "http://rahulshettyacademy.com",
                    "language": "French-IN"
                }""";
    }

    public static String coursePrice() {
        return """
                {
                  "dashboard": {
                    "purchaseAmount": 171,
                    "website": "rahulshettyacademy.com"
                  },
                  "courses": [
                    {
                      "title": "Selenium Python",
                      "price": 50,
                      "copies": 6
                    },
                    {
                      "title": "Cypress",
                      "price": 40,
                      "copies": 4
                    },
                    {
                      "title": "RPA",
                      "price": 45,
                      "copies": 10
                    },
                     {
                      "title": "Appium",
                      "price": 36,
                      "copies": 7
                    }     
                  ]
                }
                """;
    }

    public static String addBook(String isbn, String aisle) {
        return "{" +
                "\"name\":\"Learn Appium Automation with Java\"," +
                "\"isbn\":\"" + isbn + "\"," +
                "\"aisle\":\"" + aisle + "\"," +
                "\"author\":\"John foer\"" +
                "}";
    }
}
