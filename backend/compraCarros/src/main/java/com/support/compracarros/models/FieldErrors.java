package com.support.compracarros.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class FieldErrors {

    private final List<FieldError> errors;

   public FieldErrors(List<FieldError> errors) {
       this.errors = errors;
    }

    private FieldErrors(Builder builder) {
        this.errors = new ArrayList<>(builder.errors);
    }

    public Map<String, List<String>> toMap() {
        return errors.stream()
                .collect(Collectors.groupingBy(
                        FieldError::field,
                        Collectors.mapping(FieldError::message, Collectors.toList())
                ));
    }

    public static class Builder {
        private final List<FieldError> errors = new ArrayList<>();

        public Builder add(String field, String message) {
            errors.add(new FieldError(field, message));
            return this;
        }

        public Builder add(FieldError error) {
            errors.add(error);
            return this;
        }

        public Builder addAll(List<FieldError> newErrors) {
            errors.addAll(newErrors);
            return this;
        }

        public FieldErrors build() {
            return new FieldErrors(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


}
