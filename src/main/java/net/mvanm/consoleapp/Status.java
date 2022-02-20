package net.mvanm.consoleapp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
@ToString
public class Status {
private final boolean verified;
private final int sentCount;    
}
