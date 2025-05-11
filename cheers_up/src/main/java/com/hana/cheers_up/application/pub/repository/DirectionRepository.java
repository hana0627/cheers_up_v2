package com.hana.cheers_up.application.pub.repository;

import com.hana.cheers_up.application.pub.domain.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
