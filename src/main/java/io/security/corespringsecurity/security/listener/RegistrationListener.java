package io.security.corespringsecurity.security.listener;

import io.anymobi.domain.entity.users.User;
import io.anymobi.services.jpa.mail.AbstractMailService;
import io.anymobi.services.jpa.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("syncMail")
public class RegistrationListener extends AbstractMailService implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private IUserService service;

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {

        this.confirmRegistration(event);
    }

    public void confirmRegistration(Object confirm) {

        OnRegistrationCompleteEvent event = (OnRegistrationCompleteEvent)confirm;
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        SimpleMailMessage email = constructEmailMessage(event.getAppUrl(), event.getLocale(), token, user.getEmail());
        mailSender.send(email);
    }

}