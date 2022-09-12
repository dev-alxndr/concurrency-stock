package me.alxndr.conccurecystock.facade;

import lombok.RequiredArgsConstructor;
import me.alxndr.conccurecystock.service.OptimisticLockStockService;
import org.springframework.stereotype.Service;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;


    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

}
