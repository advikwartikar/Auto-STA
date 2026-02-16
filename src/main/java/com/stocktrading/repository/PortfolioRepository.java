package com.stocktrading.repository;

import com.stocktrading.model.Portfolio;
import com.stocktrading.model.User;
import com.stocktrading.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(User user);
    Optional<Portfolio> findByUserAndStock(User user, Stock stock);
    List<Portfolio> findByQuantityGreaterThan(Integer quantity);
}
