package com.humanbooster.buisinessCase.utils;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DateUtils {

    public static boolean isLocalDateTodayOrInPast(LocalDate date){
        return date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    public static boolean isLocalDateInFuture(LocalDate date){
        return date.isAfter(LocalDate.now());
    }
}
