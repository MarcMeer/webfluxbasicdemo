package net.mvanm.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class StepVerifierTest {
    @Test
    void flux_met_StepVerifier() {
        Flux<Integer> integerFlux = Flux.fromIterable(List.of("1", "2", "3")).map(Integer::valueOf);

        StepVerifier.create(integerFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete(); // Niet vergeten
    }

    @Test
    void flux_met_StepVerifier_nextMatches_nextCount() {
        Flux<Integer> integerFlux = Flux.fromIterable(List.of("1", "2", "3")).map(Integer::valueOf);

        StepVerifier.create(integerFlux)
                .expectNextMatches(v -> v>0)
                .expectNextCount(2)
                .verifyComplete();
    }
}
