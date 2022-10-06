package org.java.personal.project.util;

import org.java.personal.project.enumeration.DayHourMinuteSecondAgoEnum;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public String countTimeDifferentThenReturn (Date startDate, Date endDate) throws ParseException {

        String fromDateToString = sdf.format(startDate);
        String toDateToString = sdf.format(endDate);

        Long startTime = sdf.parse(fromDateToString).getTime();
        Long endTime = sdf.parse(toDateToString).getTime();

        Long diffSeconds = (endTime - startTime) / 1000 % 60;
        Long diffMinutes = (endTime - startTime) / (60 * 1000) % 60;
        Long diffHours = (endTime - startTime) / (60 * 60 * 1000) % 24;
        Long diffDays = (endTime - startTime) / (24 * 60 * 60 * 1000);

        if(diffDays >= 1)
            return diffDays + DayHourMinuteSecondAgoEnum.DAY.getMessage();
        if(diffHours >= 1)
            return diffHours + DayHourMinuteSecondAgoEnum.HOUR.getMessage();
        if(diffMinutes >= 1)
            return diffMinutes + DayHourMinuteSecondAgoEnum.MINUTE.getMessage();
        else
            return diffSeconds + DayHourMinuteSecondAgoEnum.SECOND.getMessage();
    }
}
