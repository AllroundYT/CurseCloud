package dev.allround.cloud.util;

import java.io.Serializable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class TimeStamp implements Serializable {
    private final long currentMillis;

    public TimeStamp() {
        this.currentMillis = System.currentTimeMillis();
    }

    public Calendar getCalendar() {
        return new Calendar.Builder().setInstant(getDate()).build();
    }

    public Date getDate() {
        return Date.from(Instant.ofEpochMilli(currentMillis));
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public Instant getInstant() {
        return Instant.ofEpochMilli(getCurrentMillis());
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "date=" + getDate() +
                '}';
    }
}
