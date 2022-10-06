package org.java.personal.project.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayHourMinuteSecondAgoEnum {
    DAY("d"),
    HOUR("h"),
    MINUTE("m"),
    SECOND("s");

    private String message;
}
