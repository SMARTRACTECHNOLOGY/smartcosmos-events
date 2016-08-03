package net.smartcosmos.events;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import net.smartcosmos.annotation.EnableSmartCosmosEvents;
import net.smartcosmos.annotation.EnableSmartCosmosSecurity;

/**
 * Launch class for the Event Service. Receives events through regular service authentication, and deposits them into the event pipeline.
 */
@EnableSmartCosmosEvents
@EnableSmartCosmosSecurity
@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventApplication.class).web(true).run(args);
    }

}
