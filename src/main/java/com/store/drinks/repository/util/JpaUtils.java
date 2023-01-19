package com.store.drinks.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.springframework.stereotype.Component;

@Component
public class JpaUtils<E> {
  
  public List<E> parseTuple(List<Tuple> tuple, Class o) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    List<E> list = (List<E>) tuple.stream()
      .map(p -> {
        Map<String, Object> maps = new LinkedHashMap<>();
        p.getElements().forEach(te -> {
          maps.put(te.getAlias(), p.get(te.getAlias()));
        });
        return mapper.convertValue(maps, o);
      })
    .collect(Collectors.toList());
    return list;
  }
}
