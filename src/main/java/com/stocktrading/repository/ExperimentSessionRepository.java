package com.stocktrading.repository;

import com.stocktrading.model.ExperimentSession;
import com.stocktrading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentSessionRepository extends JpaRepository<ExperimentSession, Long> {
    Optional<ExperimentSession> findByUserAndCompletedFalse(User user);
    List<ExperimentSession> findByUser(User user);
    List<ExperimentSession> findByCompletedTrue();
}
