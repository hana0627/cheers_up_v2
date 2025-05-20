package com.hana.cheers_up.unit.global.config.clock.impl;

import com.hana.cheers_up.global.config.clock.impl.SystemTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

class SystemTimeProviderTest {

    private SystemTimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        timeProvider = new SystemTimeProvider();
    }

    @Test
    void 현재_시간을_반환한다() throws Exception{
        //given
        long beforeTime = System.currentTimeMillis();

        //when
        sleep(1);
        long result = timeProvider.getCurrentTime();
        sleep(1);
        long afterTime = System.currentTimeMillis();

        //then
        assertThat(result).isGreaterThan(beforeTime);
        assertThat(result).isLessThan(afterTime);

    }

}
