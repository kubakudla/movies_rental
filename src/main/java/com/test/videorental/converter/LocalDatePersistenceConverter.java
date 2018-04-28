package com.test.videorental.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Converter used for converting LocalDate into java.sql.Date
 */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate entityValue) {
        return entityValue != null ? Date.valueOf(entityValue) : null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date databaseValue) {
        return databaseValue != null ? databaseValue.toLocalDate() : null;
    }
}
