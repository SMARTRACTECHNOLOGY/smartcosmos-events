package net.smartcosmos.events;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import net.smartcosmos.security.user.SmartCosmosUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rather than implement an Event listener in every since RDAO, which would cause massive
 * issues on changing out the event service, the "passive" services that merely send
 * events can just post to this endpoint.
 *
 * @author voor
 */
@RestController
@Slf4j
public class EventResource {

    private final ISmartCosmosEventGateway eventGateway;

    @Autowired
    public EventResource(final ISmartCosmosEventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @RequestMapping
    public ResponseEntity getEvent() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "**", method = RequestMethod.POST)
    public ResponseEntity postEvent(@RequestBody @Valid SmartCosmosEvent event, SmartCosmosUser user) {

        log.info("Received event of type {}, sending event contents: {}, from user {}",
                event.getEventType(), event.getData(), user.getUsername());

        eventGateway.convertAndSend(event);

        return ResponseEntity.noContent().build();
    }
}
