package ivan.rest.example.util.session;

import ivan.rest.example.exception.CustomRuntimeException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Component
public class SessionImpl implements Session {

    private ConcurrentHashMap<SessionKey, Object> dataMap;

    @PostConstruct
    private void init() {
        dataMap = new ConcurrentHashMap<>();
    }

    @Override
    public void put(SessionKey key, Object o) {
        checkAllNotNull(key, o);
        dataMap.put(key, o);
    }

    @Override
    public <T> T get(SessionKey key, Class<T> asType) {
        checkAllNotNull(key, asType);
        checkIfExist(key);
        return asType.cast(dataMap.get(key));
    }

    public void clear() {
        dataMap.clear();
    }

    private void checkIfExist(SessionKey key) {
        if (!dataMap.containsKey(key)) {
            throw new CustomRuntimeException(String.format("Entry with key = %s not found!", key));
        }
    }

    private void checkAllNotNull(Object... args) {
        Arrays.asList(args).forEach(o -> {
            if (isNull(o)) {
                throw new CustomRuntimeException("Parameter cannot be null");
            }
        });
    }
}
