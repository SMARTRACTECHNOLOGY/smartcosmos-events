package net.smartcosmos.events;

import lombok.extern.slf4j.Slf4j;
import net.smartcosmos.security.user.SmartCosmosUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

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

    private final SmartCosmosEventTemplate smartCosmosEventTemplate;

    @Autowired
    public EventResource(final SmartCosmosEventTemplate smartCosmosEventTemplate) {
        this.smartCosmosEventTemplate = smartCosmosEventTemplate;
    }

    @RequestMapping
    public ResponseEntity getEvent() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "**", method = RequestMethod.POST)
    public ResponseEntity postEvent(@RequestBody @Valid SmartCosmosEvent event, SmartCosmosUser user) {

        log.info("Received event of type {}, sending event contents: {}, from user {}",
                event.getEventType(), event.getData(), user.getUsername());

        try {
            smartCosmosEventTemplate.sendEvent(event);
        } catch (SmartCosmosEventException e) {
            log.debug(e.getMessage(),e);
            Map<String,String> error = new LinkedHashMap<>();
            error.put("code","-1");
            error.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
        }

        return ResponseEntity.noContent().build();
    }
}
