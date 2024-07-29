package com.company.module.producer;

import com.company.module.event.dto.base.EmailEvent;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailPublisher extends AbstractPublisher<EmailEvent<?>> {

    @Value("${email.sender:info@domain.com.vn,noreply@domain.com.vn,cskh@domain.com.vn}")
    private String sender;
    private final Environment environment;

    private static final String EMAIL_TEMPLATE_KEY_FORMAT = "email.template.%s.key";

    @Override
    public Class<?> getType() {
        return EmailEvent.class;
    }

    @Override
    protected void send(EmailEvent<?> emailEvent) {
        // su dung CRM
        String body = MapperUtils.writeValueAsString(emailEvent.getBody());
        String templateCode = String.format(EMAIL_TEMPLATE_KEY_FORMAT, emailEvent.getBody().getActionType());
        String templateName = environment.getProperty(templateCode);
        if (!ObjectUtils.isEmpty(templateName)) {
            log.info("Email send cc: {}, bcc: {}, subject: {}, body: {}", emailEvent.getCc(), emailEvent.getBcc(), emailEvent.getSubject(), body);
            log.info("Email send Success templateName: {}, message: {}", templateName, body);
        } else {
            log.info("Config Email templateCode for: {}", templateCode);
        }
    }
}
