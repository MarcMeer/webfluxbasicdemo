package net.mvanm.consoleapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {
    // Normaal maken we een BEAN meestal een per client, de bean heeft dan ook direct de headers etc. goed staan
    public static final WebClient WEB_CLIENT = WebClient.builder().build();

    public static void main(String args[]) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        // Integenstelling tot wat de documentatie beweerd, werkt forceren op reactive niet goed. Tomcat wordt gewoon weer
        // opgestart als starter-web ook is opgenomen in de pom. We zien echter wat inconsequent gedrag daarom adviseer
        // ik deze soms op servlet te zetten. Dan weet je zeker dat netty niet toch geladen wordt
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        // Indien we webflux niet geladen hebben dan werkt .REACTIVE ook niet, maar ik vertrouw niet erop dat REACTIVE altijd goed werkt
        springApplication.run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();



        retrieve("https://cat-fact.herokuapp.com/facts")
                .subscribe(o -> log.info("retrieve: {}", o.toString()));

        exchangeToFluxMethode("https://cat-fact.herokuapp.com/facts")
                .subscribe(o -> log.info("exchangeToFlux:{}", o.toString()));



        // subscribe blijft nonblocking, onze huidige thread is al afgesloten voordat er antwoord is...
        // door een executor te defineren en kunnen we wachten tot deze klaar is om zo nog wel antwoord te krijgen
        executor.awaitTermination(3, TimeUnit.SECONDS); //block current main thread
        executor.shutdown();// dit is alleen nodig omdat we in een console applicatie zitten
    }

    private Flux<CatFacts> retrieve(String url) {
        return WEB_CLIENT
                .get()
                .uri(url)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                // retrieve gooit een WebClientResponseException$NotFound exception indien niet gevonden.
                .bodyToFlux(CatFacts.class);// Als we geen idee hebben hoe objecten er uit zien, kunnen we linkedhashmap gebruiken
    }


    private Flux<CatFacts> exchangeToFluxMethode(String url) {
        return WEB_CLIENT
                .get()
                .uri(url)
                .accept(MediaType.TEXT_HTML)
                .exchangeToFlux(clientResponse -> {
                    // Exchange to Flux doet geen foutafhandeling ==> meer controle!
                    if(clientResponse.rawStatusCode()!=200) {
                        return Flux.error(() -> new IllegalStateException("HttpStatus was "+ clientResponse.rawStatusCode()));
                    }
                    return clientResponse.bodyToFlux(CatFacts.class);
                });
    }

}
