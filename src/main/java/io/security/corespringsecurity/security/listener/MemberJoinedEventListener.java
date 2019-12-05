package io.security.corespringsecurity.security.listener;

import io.anymobi.common.enums.EmailTemplateType;
import io.anymobi.common.enums.SmsTemplateType;
import io.anymobi.domain.dto.users.MemberDto;
import io.anymobi.services.mybatis.email.EmailService;
import io.anymobi.services.mybatis.member.EventMemberJoinService;
import io.anymobi.services.mybatis.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MemberJoinedEventListener {

    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = EventMemberJoinService.MemberJoinedEvent.class)
    public void handle(EventMemberJoinService.MemberJoinedEvent event) {
        MemberDto member = event.getMember();
        emailService.sendEmail(member.getEmail(), EmailTemplateType.JOIN);
        smsService.sendSms(member.getPhoneNo(), SmsTemplateType.JOIN);
    }
}
