package sample.rs;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sample.rs.model.RepositoryItem;
import sample.rs.model.RepositoryItemRepository;
import sample.rs.model.RepositoryItemType;

/**
 * 
 * @author Andika
 * 
 * Integration testing
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest("server.port:0")
public class ApplicationIntegrationTest {

	@Autowired
	RepositoryItemRepository itemRepo;

	@Value("${local.server.port}")
	int port;

	@Before
	public void before() {
		RestAssured.port = port;
	}

	//normal case
	@Test
	public void registerItemTest() {
		RepositoryItem newItem = new RepositoryItem("itemx", "contentx", RepositoryItemType.JSON);
		assertNull(itemRepo.findOne(newItem.getItemName()));
		
		given()
			.contentType(ContentType.JSON)
			.body(newItem)
		.when()
			.post("/api/repository")
		.then()
			.statusCode(HttpStatus.SC_CREATED)
			.header("Location", Matchers.containsString("/api/repository/itemx"));
		
		RepositoryItem item = itemRepo.findOne("itemx");
		assertNotNull(item);
		assertThat(item.getItemName(), Matchers.is("itemx"));
		assertThat(item.getItemContent(), Matchers.is("contentx"));
		assertThat(item.getItemType(), Matchers.is(RepositoryItemType.JSON));
	}
	
	@Test
	public void deleteItemTest() {
		when()
			.delete("/api/repository/{itemName}", "item1")
		.then()
			.statusCode(HttpStatus.SC_NO_CONTENT);
		
		RepositoryItem item = itemRepo.findOne("item1");
		assertNull(item);
	}
	
	@Test
	public void retrieveItemContent() {
		when()
			.get("/api/repository/{itemName}", "item2")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("itemContent", Matchers.is("{\"id\": 2, \"name\": \"itemContent2\"}"));
	}
	
	@Test
	public void retrieveItemType() {
		when()
			.get("/api/repository/{itemName}/type", "item2")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("itemType", Matchers.is(RepositoryItemType.JSON.ordinal() + 1));
	}
	//end normal case

	//exceptional case
	@Test
	public void registerAlreadyExisItemTest() {
		RepositoryItem existItem = new RepositoryItem("item2", "contenx", RepositoryItemType.JSON);
		assertNotNull(itemRepo.findOne(existItem.getItemName()));
		
		given()
			.contentType(ContentType.JSON)
			.body(existItem)
		.when()
			.post("/api/repository")
		.then()
			.statusCode(HttpStatus.SC_CONFLICT)
			.header("Location", Matchers.containsString("/api/repository/item2"));
		
	}
	
	@Test
	public void registerItemWithEmptyDataTest() {
		//no itemName specified
		RepositoryItem invalidItem = new RepositoryItem();
		invalidItem.setItemContent("invalid content");
		invalidItem.setItemType(RepositoryItemType.JSON);
		
		given()
			.contentType(ContentType.JSON)
			.body(invalidItem)
		.when()
			.post("/api/repository")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	public void deleteNonExistingItemTest() {
		when()
			.delete("/api/repository/{itemName}", "notExistItemName")
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void retrieveNonExistingItemContentTest() {
		when()
			.delete("/api/repository/{itemName}", "notExistItemName")
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void retrieveNonExistingItemTypeTest() {
		when()
			.delete("/api/repository/{itemName}", "notExistItemName")
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}
	//end exceptional case
}
