package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

import org.json.JSONObject;
import org.testng.Assert;

import java.io.FileWriter;
import java.io.IOException;

public class Login_test_api {

    private RequestSpecification request;
    private Response response;
    private String secureToken;

    @Given("I set the login API endpoint")
    public void i_set_the_login_api_endpoint() {
        RestAssured.baseURI = "http://uat.skservices.okdollar.org/RestService.svc/GetLoginResponse";
        request = RestAssured.given().header("Content-Type", "application/json");
    }

    @And("I set request body with mobile {string} and password {string}")
    public void i_set_request_body(String mobile, String password) {
        JSONObject body = new JSONObject();
        body.put("MobileNumber", mobile);
        body.put("Password", password);
        request.body(body.toString());
    }

    @When("I send the login POST request")
    public void i_send_login_post_request() {
        response = request.post();
        System.out.println("Response:\n" + response.getBody().asString());
    }

    @Then("the response status code should be {int}")
    public void validate_status_code(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @Then("the ResultCode should be {int}")
    public void validate_result_code(int expectedCode) {
        String json = response.jsonPath().getString("Data");
        JSONObject dataJson = new JSONObject(json);
        int actualCode = dataJson.getInt("ResultCode");
        Assert.assertEquals(actualCode, expectedCode);
    }

    @Then("the ResultDescription should be {string}")
    public void validate_result_description(String expectedDescription) {
        String json = response.jsonPath().getString("Data");
        JSONObject dataJson = new JSONObject(json);
        String actualDescription = dataJson.getString("ResultDescription");
        Assert.assertEquals(actualDescription, expectedDescription);
    }

    @And("I save the securetoken for reuse")
    public void extract_secure_token() {
        String json = response.jsonPath().getString("Data");

        try {
            JSONObject dataJson = new JSONObject(json);
            secureToken = dataJson.getString("securetoken");
            System.out.println("Secure Token: " + secureToken);

            // Optional: Save to file
            FileWriter writer = new FileWriter("token.txt");
            writer.write(secureToken);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error parsing secure token: " + e.getMessage());
        }
    }
}
