package net.smartcosmos.events;

import net.smartcosmos.spring.EnableSmartCosmos;
import net.smartcosmos.spring.EnableSmartCosmosSecurity;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author voor
 */
@EnableSmartCosmos
@EnableSmartCosmosEvents
@EnableSmartCosmosSecurity
@SpringBootApplication
public class EventService {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventService.class).web(true).run(args);
    }

}
