package com.hana.cheers_up.application.user.repository;

import com.hana.cheers_up.application.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
