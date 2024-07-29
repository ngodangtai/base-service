package com.company.module.producer;

import com.company.module.event.dto.base.SmsEvent;
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
public class SmsPublisher extends AbstractPublisher<SmsEvent<?>> {

    @Value("${phone.sender:0123456789}")
    private String sender;
    private final Environment environment;

    private static final String SMS_TEMPLATE_KEY_FORMAT = "sms.template.%s.key";

    @Override
    public Class<?> getType() {
        return SmsPublisher.class;
    }

    @Override
    protected void send(SmsEvent<?> smsEvent) {
        String body = MapperUtils.writeValueAsString(smsEvent.getBody());
        String templateCode = String.format(SMS_TEMPLATE_KEY_FORMAT, smsEvent.getBody().getActionType());
        String templateName = environment.getProperty(templateCode);
        if (!ObjectUtils.isEmpty(templateName)) {
            log.info("SMS send phoneNumber: {}, body: {}", smsEvent.getPhoneNumber(), body);
            log.info("SMS send Success templateName: {}, message: {}", templateName, body);
        } else {
            log.info("Config SMS templateCode for: {}", templateCode);
        }
    }
}
