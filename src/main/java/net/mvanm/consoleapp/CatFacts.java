package net.mvanm.consoleapp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Jacksonized
@Getter
@Builder
@ToString
public class CatFacts {
    private final Status status;
    private final String _id;
    private final String user;
    private final String text;
    private final int __v;
    private final String source;
    private final LocalDateTime updatedAt;
    private final String type;
    private final LocalDateTime createdAt;
    private final boolean deleted;
    private final boolean used;
}
