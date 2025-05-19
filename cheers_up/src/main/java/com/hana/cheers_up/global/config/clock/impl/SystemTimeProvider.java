package com.hana.cheers_up.global.config.clock.impl;

import com.hana.cheers_up.global.config.clock.TimeProvider;
import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProvider {

    @Override
    public Long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
