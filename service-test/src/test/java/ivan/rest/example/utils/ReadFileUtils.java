package ivan.rest.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class ReadFileUtils {
    private static final String RESOURCE_PATH = "service-test/src/test/resources/templates/";

    @Autowired
    private static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T getRequestAs(String fileTemplate, String fileName, Class<T> aClass){
        File file = new File(RESOURCE_PATH + fileTemplate + "/" + fileName + ".json");

        return mapper.readValue(file, aClass);
    }
}
