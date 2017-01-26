package net.smartcosmos.events.resource;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.smartcosmos.events.SmartCosmosEvent;
import net.smartcosmos.events.SmartCosmosEventTemplate;
import net.smartcosmos.security.user.SmartCosmosUser;

/**
 * Rather than implement an Event listener in every since RDAO, which would cause massive
 * issues on changing out the event service, the "passive" services that merely send
 * events can just post to this endpoint.
 *
 * @author voor
 */
@RestController
@Slf4j
@Profile("noop")
public class NoOpEventResource {

    @Autowired
    public NoOpEventResource(SmartCosmosEventTemplate eventService) {

        this.eventService = eventService;
    }

    private final SmartCosmosEventTemplate eventService;

    @RequestMapping
    public ResponseEntity getEvent() {

        return ResponseEntity.noContent()
            .build();
    }

    @RequestMapping(value = "**", method = RequestMethod.POST)
    public ResponseEntity postEvent(@RequestBody @Valid SmartCosmosEvent event, SmartCosmosUser user) {

        log.info("REST Received event of type {}, sending event contents: {}, from user {}",
                 event.getEventType(), event.getData(), user.getUsername());
        log.info("Doing nothing about that event because this is the no-op resource.");

        return ResponseEntity.noContent()
            .build();
    }

    @PostConstruct
    public void init() {

        log.error("\n\n***********************************************************************************\n" +
                  "WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING \n\n" +
                  "\t\tRUNNING IN NO-OP MODE, ABSOLUTELY NO EVENTS ARE SAVED OR SENT.\n" +
                  "\t\tTHIS IS A DEVELOPMENT ONLY MODE AND SHOULD NEVER BE USED IN PRODUCTION!\n" +
                  "***********************************************************************************\n");
    }
}
