package ivan.rest.example.util.testUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ivan.rest.example.exception.CustomRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Collection<T> castCollectionTo(Class<? extends T> clazzToCast, Collection<?> collection) {
        List<T> result = new ArrayList<>(collection.size());
        for (Object o : collection) {
            try {
                result.add(clazzToCast.cast(o));
            } catch (ClassCastException e) {
                throw new CustomRuntimeException(String.format("Object %s does NOT belongs to the class %s", o.toString(), clazzToCast.getName()));
            }
        }
        return result;
    }

    public static <T> T convertValue(Object valueToConvert, TypeReference<T> typeReference) {
        if (valueToConvert instanceof List) {
            return objectMapper.convertValue(valueToConvert, typeReference);
        } else {
            return objectMapper.convertValue(List.of(valueToConvert), typeReference);
        }
    }
}
