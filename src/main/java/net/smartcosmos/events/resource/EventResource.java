package net.smartcosmos.events.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
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
@ConditionalOnProperty(name = "smartcosmos.events.noop", havingValue = "false", matchIfMissing = true)
public class EventResource {

    @Autowired
    public EventResource(SmartCosmosEventTemplate eventService) {
        this.eventService = eventService;
    }

    private final SmartCosmosEventTemplate eventService;

    @RequestMapping
    public ResponseEntity getEvent() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "**", method = RequestMethod.POST)
    public ResponseEntity postEvent(@RequestBody @Valid SmartCosmosEvent event, SmartCosmosUser user) {

        log.info("REST Received event of type {}, sending event contents: {}, from user {}",
                event.getEventType(), event.getData(), user.getUsername());

        MessageBuilder builder = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, event.getEventType());

        if (StringUtils.hasText(event.getEventUrn())) {
            builder.setHeader(KafkaHeaders.MESSAGE_KEY,event.getEventUrn());
        }

        try {
            eventService.sendEvent(event);
        } catch (Exception e) {
            log.debug(e.getMessage(),e);
            Map<String,String> error = new LinkedHashMap<>();
            error.put("code","-1");
            error.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
        }

        return ResponseEntity.noContent().build();
    }
}
