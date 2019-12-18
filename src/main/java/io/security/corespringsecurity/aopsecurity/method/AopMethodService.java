package io.security.corespringsecurity.aopsecurity.method;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AopMethodService {

    public void methodTest() {
        log.debug("methodTest");
    }

    public void methodTest2(AopMethodService methodService) {
        methodService.innerCallMethodTest();
        log.debug("methodTest2");
    }

    public void methodTest3() {
        log.debug(this.getClass().getSimpleName());
        this.innerCallMethodTest();
        log.debug("methodTest2");
    }

    public void innerCallMethodTest() {
        log.debug("innerCallMethodTest");
    }

}
