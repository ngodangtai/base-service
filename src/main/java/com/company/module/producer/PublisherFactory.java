package com.company.module.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PublisherFactory {
    private final Map<Class<?>, Publisher> lookup;

    public PublisherFactory(List<Publisher> publishers) {
        this.lookup = new HashMap<>();
        publishers.forEach(p -> lookup.put(p.getType(), p));
    }

    public Publisher getInstance(Class<?> clazz) {
        return lookup.get(clazz);
    }
}
