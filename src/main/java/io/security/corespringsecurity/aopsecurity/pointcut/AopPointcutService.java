package io.security.corespringsecurity.aopsecurity.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AopPointcutService {

    public void aopService(){
      log.debug("AopFirstService");
    }
}
