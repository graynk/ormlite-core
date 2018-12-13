package com.j256.ormlite.field.types;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseResults;

/**
 * A custom persister that is able to store the java.time.LocalDate class in the database as Date object.
 *
 * @author graynk
 */
public class LocalTimeType extends BaseLocalDateType {

    private static LocalTimeType singleton;
    public static LocalTimeType getSingleton() {
        if (singleton == null) {
            try {
                Class.forName("java.time.LocalTime", false, null);
                singleton = new LocalTimeType();
            } catch (ClassNotFoundException e) {
                return null; // No java.time on classpath;
            }
        }
        return singleton;
    }
    private LocalTimeType() { super(SqlType.LOCAL_TIME, new Class<?>[] { LocalTime.class }); }
    protected LocalTimeType(SqlType sqlType, Class<?>[] classes) { super(sqlType, classes); }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        try {
            return LocalTime.parse(defaultStr, DateTimeFormatter.ofPattern("HH:mm:ss[.SSSSSS]"));
        } catch (NumberFormatException e) {
            throw SqlExceptionUtil.create("Problems with field " + fieldType +
                    " parsing default LocalTime value: " + defaultStr, e);
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getLocalTime(columnPos);
    }

    @Override
    public Object moveToNextValue(Object currentValue) {
        LocalTime time = (LocalTime) currentValue;
        return time.plusNanos(1);
    }

    @Override
    public boolean isValidForField(Field field) {
        return (field.getType() == LocalTime.class);
    }
}
