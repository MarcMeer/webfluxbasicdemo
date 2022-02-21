package net.mvanm.webflux;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@Slf4j
public class ErrorHandlingTest {
    @Test
    void help_een_error() {
        Mono<MonosTest.OnzinObject> producent = Mono.just("de producents waarde")
                .map(ErrorHandlingTest::creerFout)
                .doOnNext(v -> log.info("Wat loopt er door de stream: {}",v)) // Bij een fout wordt dit niet gedaan
                .map(v -> "dit is toegevoegd aan: " + v) // Bij een fout worden ook de maps daarna niet meer uitgevoerd
                .onErrorReturn("het ging fout") // hier geven we aan wat we moeten doen bij een fout
                .onErrorMap(MyCheckException::new) // Fout omzetten in een andere fout, dit mag een typed exception zijn
                .map(MonosTest.OnzinObject::new);

        log.info("De producent is ingesteld");

        // subscriben, Als we een fout niet afvangen zal deze hier gegooid worden, alle maps worden gewoon geskipped!
        producent.subscribe(value -> {
            log.info("Er is een object ontvangen {}", value.getValue());
        });

    }

    private static String creerFout(String s) {
        throw new IllegalStateException("Dit mag dus niet gebeuren!");
    }

    @Getter
    @AllArgsConstructor
    public static class OnzinObject {
        private final String value;

    }


    private static class MyCheckException extends Exception {
        public MyCheckException(Throwable throwable) {
            super(throwable);
        }
    }
}
