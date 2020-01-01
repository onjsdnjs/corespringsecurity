package io.security.corespringsecurity.aopsecurity.method;

import org.springframework.stereotype.Service;

@Service
public class AopMethodService {

    public void notMethodSecured() {
        System.out.println("notMethodSecured");
    }

    public void methodSecured() {
        System.out.println("methodSecured");
    }
}



