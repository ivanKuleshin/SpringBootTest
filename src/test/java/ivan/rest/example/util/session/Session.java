package ivan.rest.example.util.session;

public interface Session {

    void put(SessionKey key, Object object);

    <T> T get(SessionKey key, Class<T> asType);

    void clear();
}
