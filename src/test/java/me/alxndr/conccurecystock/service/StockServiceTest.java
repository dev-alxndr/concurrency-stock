package me.alxndr.conccurecystock.service;

import me.alxndr.conccurecystock.domain.Stock;
import me.alxndr.conccurecystock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@SpringBootTest
class StockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소 테스트")
    public void stock_decrease() {
        long productId = 1L;
        stockService.decrease(productId, 1L);

        Stock stock = getStock(1L);
        assertThat(stock.getQuantity()).isEqualTo(99);
    }

    private Stock getStock(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        return stock;
    }

    @Test
    public void 동시에_100_요청() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);;
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = getStock(1L);
        assertThat(stock.getQuantity()).isEqualTo(0L);

    }
}
