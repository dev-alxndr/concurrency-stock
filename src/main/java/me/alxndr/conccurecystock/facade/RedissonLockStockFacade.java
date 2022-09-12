package me.alxndr.conccurecystock.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.alxndr.conccurecystock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;

    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) {

        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean hasLock = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!hasLock) {
                log.debug("Lock 획득 실패");
                return;
            }

            stockService.decrease(id, quantity);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
