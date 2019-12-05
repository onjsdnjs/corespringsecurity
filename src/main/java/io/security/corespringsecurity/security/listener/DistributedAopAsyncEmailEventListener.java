package io.security.corespringsecurity.security.listener;

import io.anymobi.domain.dto.SendableParameter;
import io.anymobi.services.mybatis.email.EmailService;
import io.anymobi.services.mybatis.member.DistributedAopAsyncEventMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DistributedAopAsyncEmailEventListener {

    @Autowired
    private EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DistributedAopAsyncEventMemberService.DistributedAopAsyncMemberJoinedEvent.class)
    public void handleMemberJoinedEvent(DistributedAopAsyncEventMemberService.DistributedAopAsyncMemberJoinedEvent event) {
        SendableParameter parameter = event.getValue();
        emailService.sendEmail(parameter.getEmail(), parameter.getEmailTemplateType());
    }
}
