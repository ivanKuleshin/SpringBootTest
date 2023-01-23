package ivan.rest.example.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class ReadFileHelper {
    private static final String RESOURCE_PATH = "src/test/resources/templates/";

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T getRequestAs(String fileTemplate, String fileName, Class<T> aClass){
        File file = new File(RESOURCE_PATH + fileTemplate + "/" + fileName + ".json");

        return mapper.readValue(file, aClass);
    }
}
