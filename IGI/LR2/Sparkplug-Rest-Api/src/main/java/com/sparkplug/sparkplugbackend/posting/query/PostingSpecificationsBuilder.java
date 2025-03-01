package com.sparkplug.sparkplugbackend.posting.query;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostingSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public PostingSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final PostingSpecificationsBuilder with(String key, String searchOperation, Object value) {
        params.add(new SearchCriteria(key, searchOperation, value));
        return this;
    }

    public Specification<Posting> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<Posting> result = new PostingSpecification(params.getFirst());

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new PostingSpecification(params.get(i)));
        }

        return result;
    }


}
