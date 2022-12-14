package me.alxndr.conccurecystock.service;

import lombok.RequiredArgsConstructor;
import me.alxndr.conccurecystock.domain.Stock;
import me.alxndr.conccurecystock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockRepository stockRepository;


    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        stock.decrease(quantity);

        stockRepository.save(stock);
    }
}
