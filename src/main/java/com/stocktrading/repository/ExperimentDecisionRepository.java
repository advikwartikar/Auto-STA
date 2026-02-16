package com.stocktrading.repository;

import com.stocktrading.model.ExperimentDecision;
import com.stocktrading.model.ExperimentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExperimentDecisionRepository extends JpaRepository<ExperimentDecision, Long> {
    List<ExperimentDecision> findBySession(ExperimentSession session);
    List<ExperimentDecision> findBySessionOrderByStockIndexAscDayNumberAsc(ExperimentSession session);
}
