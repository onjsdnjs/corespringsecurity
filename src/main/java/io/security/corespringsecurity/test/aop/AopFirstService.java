package io.security.corespringsecurity.test.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AopFirstService {

    public void aopService(){
      log.debug("AopFirstService");
    }
}
