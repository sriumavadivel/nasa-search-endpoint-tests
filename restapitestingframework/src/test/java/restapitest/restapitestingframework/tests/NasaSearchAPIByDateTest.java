package restapitest.restapitestingframework.tests;
import org.testng.asserts.*;

import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.*;
import restapitest.restapitestingframework.apiconfig.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class NasaSearchAPIByDateTest {
	final String baseUrl = ApiEndpoints.GET_BASE_URI;
	final String basePath = ApiEndpoints.GET_SEARCH_IMAGES;
	static Response response;
	String responseBody;
	String searchText = "mars";
	String mediaType = "image";
	LocalDate currentDate = LocalDate.now();
	int year = currentDate.getYear();
	 
	@BeforeClass
	public void setup() 
	{
		RestAssured.baseURI = baseUrl;
		RestAssured.basePath = basePath;
	}
	
	@Test(enabled=true, description="happy path - test for optional parameters combinations")
	public void searchWithOptionalParameters()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("q", searchText);
		httpRequest.queryParam("media_type", mediaType);
		httpRequest.queryParam("year_start", (year-10));
		httpRequest.queryParam("year_end", year);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);			
	}
	
	@Test(enabled=true, description="test for optional parameters combinations and year_start is invalid")
	public void searchWithInvalidStartYear()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("q", searchText);
		httpRequest.queryParam("media_type", mediaType);
		httpRequest.queryParam("year_start", "0000");
		httpRequest.queryParam("year_end", year);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 400);			
		Assert.assertEquals(response.path("reason"), "Invalid value year_start=0000.");
	}
	
	@Test(enabled=true, description="test for optional parameters combinations and year_end is invalid")
	public void searchWithInvalidEndYear()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("q", searchText);
		httpRequest.queryParam("media_type", mediaType);
		httpRequest.queryParam("year_start", year-5);
		httpRequest.queryParam("year_end", "0000");
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 400);			
		Assert.assertEquals(response.path("reason"), "Invalid value year_end=0000.");
	}
	
	@Test(enabled=true, description="test for optional parameters combinations and year_start is in future")
	public void searchWithStartYearInFuture()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("q", searchText);
		httpRequest.queryParam("media_type", mediaType);
		httpRequest.queryParam("year_start", year+20);
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);			
		
		//verify collection items size
		Assert.assertEquals(((Integer) response.path("collection.items.size")).intValue(), 0);
	}
}
