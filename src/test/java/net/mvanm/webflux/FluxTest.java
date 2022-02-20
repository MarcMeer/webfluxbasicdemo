package net.mvanm.webflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class FluxTest {
    @Test
    void een_producer_webflux() {
        Flux.fromIterable(List.of("1", "2", "3"))
                .map(Integer::valueOf)
                .doOnNext(w -> log.info("Waarde {}", w))
                .subscribe(i-> log.info("subscribe {}", i));

    }

}
