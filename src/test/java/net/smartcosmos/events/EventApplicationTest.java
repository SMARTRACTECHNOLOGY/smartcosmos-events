package net.smartcosmos.events;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.smartcosmos.test.security.WithMockSmartCosmosUser;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { EventApplication.class })
@ActiveProfiles("test")
@WebAppConfiguration
@WithMockSmartCosmosUser
public class EventApplicationTest {

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("kafka.broker.address", embeddedKafka.getBrokersAsString());
    }

    protected MediaType contentType = MediaType.APPLICATION_JSON_UTF8;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .get();

        Assert.assertNotNull("the JSON message converter must not be null",
                             this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(getClass());

        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    protected String json(Object o) throws IOException {

        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON,
                                                       mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void thatYouCanSendEvent() throws Exception {
        //
        // test that basic object creation works
        //
        Map<String, Object> eventBody = new LinkedHashMap<>();
        eventBody.put("eventType", "stupid-event");
        eventBody.put("data", "frog");

        MvcResult mvcResult = this.mockMvc.perform(
            post("/someType/someUrn").content(json(eventBody))
                .contentType(contentType))
            .andExpect(status().isNoContent())
            .andReturn();

    }
}
