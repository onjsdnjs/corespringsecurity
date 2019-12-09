package io.security.corespringsecurity.test.liveaop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LiveAopFirstService {

    public void liveAopService(){
      log.debug("LiveAopFirstService");
    }
}
