package net.smartcosmos.events;

import net.smartcosmos.spring.EnableSmartCosmos;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author voor
 */
@EnableSmartCosmos
@SmartCosmosEventProducer
@SpringBootApplication
public class EventService {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventService.class).web(true).run(args);
    }

}
