

package com.store.drinks.repository.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JpaUtils<E> {
  
  public List<E> converterTupleInDataTransferObject(List<Tuple> tuple, Class o) {
    List<E> list = (List<E>) tuple.stream()
      .map(p -> {
        Map<String, Object> maps = new LinkedHashMap<>();
        p.getElements().forEach(te -> {
          maps.put(te.getAlias(), p.get(te.getAlias()));
        });
        return new ModelMapper().map(maps, o);
      })
    .collect(Collectors.toList());
    return list;
  }
}
