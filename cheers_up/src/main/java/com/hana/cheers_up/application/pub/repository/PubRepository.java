package com.hana.cheers_up.application.pub.repository;

import com.hana.cheers_up.application.pub.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PubRepository extends JpaRepository<Pub, Long> {
}
