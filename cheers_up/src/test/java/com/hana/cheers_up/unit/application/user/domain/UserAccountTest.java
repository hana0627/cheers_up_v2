package com.hana.cheers_up.unit.application.user.domain;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTest {


    @Test
    void protected_기본생성자_테스트() throws Exception {
        //given
        Constructor<UserAccount> constructor = UserAccount.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        //when
        UserAccount userAccount = constructor.newInstance();

        //then
        assertThat(userAccount).isNotNull();
        assertThat(userAccount.getUserId()).isNull();
        assertThat(userAccount.getEmail()).isNull();
        assertThat(userAccount.getNickname()).isNull();
        assertThat(userAccount.getMemo()).isNull();
        assertThat(userAccount.getRoleType()).isNull();
    }



    @Test
    void EqualsAndHashCodeTest_UserId() {
        // given
        UserAccount userAccount1 = UserAccount.of("user123", "user@example.com", "사용자", "메모", RoleType.ADMIN);
        UserAccount userAccount2 = UserAccount.of("user123", "user@example.com", "사용자", "메모", RoleType.ADMIN);
        // userId 다름
        UserAccount userAccount3 = UserAccount.of("user456", "user@example.com", "다른사용자", "다른메모", RoleType.MANAGER);
        // email 다름
        UserAccount userAccount4 = UserAccount.of("user123", "other@example.com", "다른사용자", "다른메모", RoleType.MANAGER);
        // userId, email 둘 다 다름
        UserAccount userAccount5 = UserAccount.of("user456", "other@example.com", "다른사용자", "다른메모", RoleType.MANAGER);
        Object otherObject = new Object();

        // when & then
        // 동일성, 대칭성 테스트
        assertThat(userAccount1).isEqualTo(userAccount1);
        assertThat(userAccount1).isEqualTo(userAccount2);
        assertThat(userAccount2).isEqualTo(userAccount1);

        // 다른 값 테스트
        assertThat(userAccount1).isNotEqualTo(userAccount3);
        assertThat(userAccount1).isNotEqualTo(userAccount4);
        assertThat(userAccount1).isNotEqualTo(userAccount5);

        // null 및 다른 타입 테스트
        assertThat(userAccount1).isNotEqualTo(null);
        assertThat(userAccount1).isNotEqualTo(otherObject);

        // hashCode 테스트
        assertThat(userAccount1.hashCode()).isEqualTo(userAccount2.hashCode());
        assertThat(userAccount1.hashCode()).isNotEqualTo(userAccount3.hashCode());
        assertThat(userAccount1.hashCode()).isNotEqualTo(userAccount4.hashCode());
        assertThat(userAccount1.hashCode()).isNotEqualTo(userAccount5.hashCode());
    }

}
