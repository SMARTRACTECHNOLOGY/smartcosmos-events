package net.smartcosmos.events;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;

/**
 * @author voor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { EventApplication.class })
@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest({ "spring.cloud.config.enabled=false", "eureka.client.enabled:false" })
public class EventApplicationTest {

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1);

    @Value("${local.server.port}")
    private int port;

    private RequestSpecification requestSpecification;

    @Before
    public void setUp() throws Exception {
        embeddedKafka.getBrokersAsString();
        String brokerAddress = System.getProperty(KafkaEmbedded.SPRING_EMBEDDED_KAFKA_BROKERS);

        RestAssured.port = port;
        RestAssured.authentication = basic("user", "password");
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setContentType(ContentType.JSON);

        requestSpecification = builder.build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void thatYouCanSendEvent() {
        //
        // test that basic object creation works
        //
        Map<String, Object> eventBody = new LinkedHashMap<>();
        eventBody.put("eventType", "stupid-event");
        eventBody.put("data", "frog");

        // @formatter:off
        given()
            .spec(requestSpecification)
            .body(eventBody)
            .when()
            .post("/")
            .then()
            .statusCode(204)
            .log();
        // @formatter:on
    }
}
