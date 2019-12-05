package io.security.corespringsecurity.security.listener;

import io.anymobi.domain.dto.SendableParameter;
import io.anymobi.services.mybatis.member.DistributedAopAsyncEventMemberService;
import io.anymobi.services.mybatis.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DistributedAopAsyncSmsEventListener {

    @Autowired
    private SmsService smsEventService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DistributedAopAsyncEventMemberService.DistributedAopAsyncMemberJoinedEvent.class)
    public void sendEmail(DistributedAopAsyncEventMemberService.DistributedAopAsyncMemberJoinedEvent event) {
        SendableParameter request = event.getValue();
        smsEventService.sendSms(request.getPhoneNo(), request.getSmsTemplateType());
    }
}
