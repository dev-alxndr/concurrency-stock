package me.alxndr.conccurecystock.facade;

import lombok.RequiredArgsConstructor;
import me.alxndr.conccurecystock.repository.RedisLockRepository;
import me.alxndr.conccurecystock.service.StockService;
import org.springframework.stereotype.Component;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;

    private final StockService stockService;


    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }

}
