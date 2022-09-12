package me.alxndr.conccurecystock.facade;

import lombok.RequiredArgsConstructor;
import me.alxndr.conccurecystock.repository.LockRepository;
import me.alxndr.conccurecystock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }

}
