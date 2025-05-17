package com.hana.cheers_up.application.user.domain;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class UserAccount extends AuditingFields {

    @Id
    @Column(length = 50)
    private String userId; //id값
    @Column(length = 100)
    private String email; // 이메일
    @Column(length = 100)
    private String nickname; // 닉네임

    @Column(length = 100)
    private String memo; // 메모

    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 권한정보

    protected UserAccount() {
    }

    public UserAccount(String userId, String email, String nickname, String memo, RoleType roleType, String createdBy) {
        //TODO 원래는 JpaAudting field를 토해서 createdBy를 넣어주려고 했으나, null이 넘어옴
        // 결국 id값을 넣었는데 별로 바람직하지 않아보임
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.roleType = roleType;
        this.createdBy = userId;
        this.modifiedBy = userId;
    }


    public static UserAccount of(String userId, String email, String nickname, String memo, RoleType roleType) {
        return UserAccount.of(userId, email, nickname, memo, roleType, null);
    }

    public static UserAccount of(String userId,String email, String nickname, String memo, RoleType roleType, String createdBy) {
        return new UserAccount(userId, email, nickname, memo, roleType, createdBy);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(userId, that.userId) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}
