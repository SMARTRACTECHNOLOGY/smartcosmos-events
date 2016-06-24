package net.smartcosmos.events;

import net.smartcosmos.spring.EnableSmartCosmos;
import net.smartcosmos.spring.EnableSmartCosmosSecurity;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Launch class for the Event Service. Receives events through regular service authentication, and deposits them into the event pipeline.
 */
@EnableSmartCosmos
@EnableSmartCosmosEvents
@EnableSmartCosmosSecurity
@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventApplication.class).web(true).run(args);
    }

}
