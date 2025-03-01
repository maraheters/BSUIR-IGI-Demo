package com.sparkplug.sparkplugbackend.posting.query;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

public class PostingSpecification implements Specification<Posting> {

    private final SearchCriteria searchCriteria;

    public PostingSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(
            @NotNull Root<Posting> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder builder) {

        String key = searchCriteria.key();
        String operation = searchCriteria.operation();
        Object value = searchCriteria.value();

        Path<? extends String> path = extractObjectByPath(key, root);

        if(operation.equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(path, value.toString());

        } else if(operation.equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(path, value.toString());

        } else if(operation.equalsIgnoreCase(":")) {

            if(path.getJavaType() == String.class) {
                return builder.like((Expression<String>) path, "%" + value + "%");
            } else {
                return builder.equal(path, value);
            }

        }

        return null;
    }

    private static <T> Path<? extends String> extractObjectByPath(String key, Root<T> root) {
        String[] parts = key.split("\\.");
        Path<? extends String> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }

}
