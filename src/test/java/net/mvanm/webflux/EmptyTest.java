package net.mvanm.webflux;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class EmptyTest {

    @BeforeEach
    void loglevel() {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);
    }

    @Test
    void test_something() throws InterruptedException {
    }
}
