package com.mindex.challenge.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateDeserializer extends StdDeserializer<LocalDate> {
    protected DateDeserializer(Class<?> vc) {
        super(vc);
    }

    public DateDeserializer() {
        this(null);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            return LocalDate.parse(p.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM-dd");
        }
    }
}
