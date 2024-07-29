package com.company.module.producer;

import com.company.module.event.dto.base.InboxEvent;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class InboxPublisher extends AbstractPublisher<InboxEvent<?>> {

    private final Environment environment;

    private static final String INBOX_TEMPLATE_KEY_FORMAT = "inbox.template.%s.key";

    @Override
    public Class<?> getType() {
        return InboxEvent.class;
    }

    @Override
    protected void send(InboxEvent<?> inboxEvent) {
        String body = MapperUtils.writeValueAsString(inboxEvent.getBody());
        String templateCode = String.format(INBOX_TEMPLATE_KEY_FORMAT, inboxEvent.getBody().getActionType());
        String templateName = environment.getProperty(templateCode);
        if (!ObjectUtils.isEmpty(templateName)) {
            log.info("Inbox send receiver: {}, subject: {}, body: {}", inboxEvent.getReceiver(), inboxEvent.getSubject(), body);
            log.info("Inbox send Success templateName: {}, message: {}", templateName, body);
        } else {
            log.info("Config Inbox templateCode for: {}", templateCode);
        }
    }
}
