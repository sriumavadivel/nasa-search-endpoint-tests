package restapitest.restapitestingframework.tests;
import org.testng.asserts.*;
import org.testng.Assert;
import org.testng.annotations.*;
import restapitest.restapitestingframework.apiconfig.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class NasaSearchByMediaTypeTest {
	final String baseUrl = ApiEndpoints.GET_BASE_URI;
	final String basePath = ApiEndpoints.GET_SEARCH_IMAGES;
	static Response response;
	String responseBody;
	String[] searchText = { "image", "VIDEO", "Audio", "image123", null, ""};
	 
	@BeforeClass
	public void setup() 
	{
		RestAssured.baseURI = baseUrl;
		RestAssured.basePath = basePath;
	}
	
	@Test(enabled=true, description="happy path - search by valid media type in lowercase")
	public void searchWithValidTextInLowercase()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[0]);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);	
		
		//verify the elements of collection 
		//verify collection has href
		String expectedUrl = "https://images-api.nasa.gov/search?media_type=image";
		Assert.assertEquals(response.path("collection.href"), expectedUrl);
		
		//verify version
		Assert.assertEquals(response.path("collection.version"), "1.0");
		
		//verify metadata is not null
		Assert.assertNotNull(response.path("collection.metadata"));
				
		//verify collection items size
		Assert.assertEquals(((Integer) response.path("collection.items.size")).intValue(), 100);
		
		//verify collection items href is not null
		Assert.assertNotNull(response.path("collection.items.href"));
		
		//verify collection items links return 100 records
		Assert.assertEquals(((Integer) response.path("collection.items.links.size")).intValue(), 100);
		
		//verify collection items data returns 100 records
		Assert.assertEquals(((Integer) response.path("collection.items.data.size")).intValue(), 100);					
	}
	
	@Test(enabled=true, description="happy path - search by valid media type in uppercase")
	public void searchWithValidTextInUppercase()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[1]);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);	
		
		//verify collection has href
		String expectedUrl = "https://images-api.nasa.gov/search?media_type=VIDEO";
		Assert.assertEquals(response.path("collection.href"), expectedUrl);
	}
	
	@Test(enabled=true, description="happy path - search by valid media type in mixedcase")
	public void searchWithValidTextInMixedcase()
	{
			
		//Get the request specification that will be sent to server
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[2]);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);	
		
		//verify collection has href
		String expectedUrl = "https://images-api.nasa.gov/search?media_type=Audio";
		Assert.assertEquals(response.path("collection.href"), expectedUrl);
	}
	
	@Test(enabled=true, description="search by invalid media type")
	public void searchWithInvalidyString()
	{
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[3]);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		
		//verify status code
		Assert.assertEquals(response.getStatusCode(), 200);	
		
		//verify collection items size
		Assert.assertEquals(((Integer) response.path("collection.items.size")).intValue(), 0);
		
		//verify collection has href
		String expectedUrl = "https://images-api.nasa.gov/search?media_type=image123";
		Assert.assertEquals(response.path("collection.href"), expectedUrl);		
	}
	
	
	@Test(enabled=true, description="media type is null")
	public void searchWithNull()
	{
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[4]);
		
				
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		Assert.assertEquals(response.getStatusCode(), 400);	
		Assert.assertEquals(response.path("reason"), "Expected 'q' text search parameter or other keywords.");		
	}
	
	@Test(enabled=true, description="media type is empty string")
	public void searchWithEmptyString()
	{
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.queryParam("media_type", searchText[5]);
					
		//Send a request to server and get response
		response = httpRequest.get();
		responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody); 
		Assert.assertEquals(response.getStatusCode(), 400);	
		Assert.assertEquals(response.path("reason"), "Expected 'q' text search parameter or other keywords.");
	}
}

