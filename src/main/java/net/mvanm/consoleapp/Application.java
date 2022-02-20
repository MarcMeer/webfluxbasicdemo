package net.mvanm.consoleapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {
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

        String baseUrl = "https://cat-fact.herokuapp.com/";
        String path = "facts";
        WebClient.builder().baseUrl(baseUrl)
                .build()
                .get()
                .uri(path)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onRawStatus(httpStatus -> httpStatus!=200, cr -> Mono.error(new IllegalStateException("HttpStatus was " + cr.rawStatusCode())))
                .bodyToFlux(CatFacts.class)// Als we geen idee hebben hoe objecten er uit zien, kunnen we linkedhashmap gebruiken
                .subscribe(o -> System.out.println(o.toString()));

        // subscribe blijft nonblocking, onze huidige thread is al afgesloten voordat er antwoord is...
        // door een executor te defineren en kunnen we wachten tot deze klaar is om zo nog wel antwoord te krijgen
        executor.awaitTermination(3, TimeUnit.SECONDS); //block current main thread
        executor.shutdown();// dit is alleen nodig omdat we in een console applicatie zitten
    }
}
