package me.alxndr.conccurecystock.repository;

import me.alxndr.conccurecystock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
public interface StockRepository extends JpaRepository<Stock, Long> {
}
