package org.acme.conference.speaker;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.quarkus.panache.mock.PanacheMock;
import org.mockito.Mockito;
import java.util.Collections;

@QuarkusTest
public class SpeakerResourceTest {

  @Inject DeterministicIdGenerator idGenerator;

  @Test
  public void testNewSpeaker() {

    UUID uuid = new UUID(1, 1);

    idGenerator.setNextUUID(uuid);
    
    given()
    .when()
      .body("{\"nameFirst\": \"Jordi\",\"nameLast\": \"Sola\"}")
      .contentType(ContentType.JSON)
      .post("/speaker")
    .then()
      .statusCode(200)
      .body("nameFirst", is("Jordi"))
      .body("nameLast", is("Sola"))
      .body("uuid", is(uuid.toString()));
  }


  @Test
  public void testListAll() {
    PanacheMock.mock(Speaker.class);
    Mockito.when(Speaker.listAll())
      .thenReturn(Collections.emptyList());

    given()
    .when()
      .get("/speaker")
    .then()
      .statusCode(200).body("size()", is(0));
  }

}
