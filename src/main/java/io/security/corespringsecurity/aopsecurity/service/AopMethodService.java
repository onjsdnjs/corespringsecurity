package io.security.corespringsecurity.aopsecurity.service;

import org.springframework.stereotype.Service;

@Service
public class AopMethodService {

    public void methodSecured() {
        System.out.println("methodSecured");
    }
}
