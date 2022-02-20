package net.mvanm.webflux;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTest {
    @Test
    void integerlist_naar_string_de_oude_manier(){
        List<Integer> resultList= new ArrayList<>();
        for(String getal:List.of("1","2","3")) {
            resultList.add(Integer.valueOf(getal));
        }
        assertThat(resultList).isEqualTo(List.of(1,2,3));
    }

    @Test
    void stream_string_naar_integer() {

        List<Integer> integerList = List.of("1", "2", "3").stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        assertThat(integerList).isEqualTo(List.of(1,2,3));
    }
}
