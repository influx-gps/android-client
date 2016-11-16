package com.gut.follower.commons;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class DateConverterTest {

    @Mock
    SimpleDateFormat simpleDateFormat;

    @Test
    public void shouldConvertTwoUnixTimeStampsToFormattedTimeDuration(){
        long startTime = 1475327105000L;
        long endTime =1475331630000L;
        String timeDuration = DateConverter.convertToTime(startTime, endTime);
        assertThat(timeDuration, is("1:15:25"));
    }

    @Test
    public void shouldFormatUnixStampToDate(){
        long startTime = 1475327105000L;
        String date = DateConverter.convertDate(startTime);
        assertThat(date, is("01-10-2016"));
    }

    @Test
    public void shouldFormatUnixStampToDateWithTime(){
        long startTime = 1475327105000L;
        String date = DateConverter.convertDateWithTime(startTime, TimeZone.getTimeZone("GMT+2"));
        assertThat(date, is("01-10-2016 15:05"));
    }

}
