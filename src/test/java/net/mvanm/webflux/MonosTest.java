package net.mvanm.webflux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MonosTest {
    @Test
    void producerTest() {
        Mono<String> stringProducer = Mono.just("een waarde")
                .doOnNext(c -> log.info("Er loopt een waarde door de stream {}", c));;

        log.info("Waarde opgehaald", stringProducer);

        stringProducer.subscribe(v -> log.info(v));// geef me een waarde
        String waarde  = stringProducer.block(); // wacht op een waarde

        log.info("Waarde na block {}", waarde);

    }

    @Test
    void webfluxTest() {
        Mono<OnzinObject> producent = Mono.just("de producents waarde")
                .doOnNext(v -> log.info("Wat loopt er door de stream: {}",v))
                .map(OnzinObject::new);

        log.info("De producent is ingesteld");

        // subscriben
        producent.subscribe(value -> {
            log.info("Er is een object ontvangen {}", value.getValue());
        });

        OnzinObject onzinObject = producent.block();
        log.info("onzin object waarde is {}", onzinObject.getValue());
        log.info("We zijn klaar met alles");
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class OnzinObject {
        private final String value;
    }



}
