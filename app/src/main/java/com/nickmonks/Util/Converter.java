package com.nickmonks.Util;

import androidx.room.TypeConverter;

import com.nickmonks.model.Priority;

import java.util.Date;

// ROOM doesnt know which type is the enum or the Date
// So we create converter to fix this.

// We annotate the methods and pass it to the DATABASE
public class Converter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromPriority(Priority priority){
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority){
        return priority == null ? null : Priority.valueOf(priority);
    }
}
