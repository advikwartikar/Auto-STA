package com.stocktrading.repository;

import com.stocktrading.model.ExperimentStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ExperimentStockRepository extends JpaRepository<ExperimentStock, Long> {
    Optional<ExperimentStock> findBySequenceOrder(Integer order);
    List<ExperimentStock> findAllByOrderBySequenceOrderAsc();
}
