package net.mvanm.webflux;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Test
    void flux_met_een_fout() {
        Flux<Integer> integerFlux = Flux.fromIterable(List.of("1", "2", "3", "4"))
                .map(this::errorWanneerEen)
                .doOnNext(s -> log.info("Waarde door stream {}", s))
                // Met onErrorContinue loggen we de fout en bij behorende waarde, stream zal de andere waardes wel netjes
                // afhandelen. Dit kan handig zijn als je met streams werkt waarbij bij een exception niet de gehele stream
                // moet falen. Indien je onderstaande regel uitzet, zal er geen enkele waarde nog naar de subscribere worden
                // gezet maar de stream een exception gooien.
                .onErrorContinue((throwable, o) -> log.error("Exception {} bij waarde {}",throwable.getMessage(), o.toString()))
                .map(Integer::valueOf);

        integerFlux.subscribe(i->log.info("Subscriber krijgt {}", i.toString()));
    }

    private String errorWanneerEen(String s) {
        if(s.equals("1")) {
            throw new IllegalStateException("Ik wil geen 1!");
        }
        return s;
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
