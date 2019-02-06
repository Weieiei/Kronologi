package appointmentscheduler.serializer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperFactory {
    public <T> ObjectMapper createMapper(Class<? extends T> clazz, JsonSerializer<T> serializer) {
        final ObjectMapper mapper = new ObjectMapper();
        final SimpleModule module = new SimpleModule();

        module.addSerializer(clazz, serializer);
        mapper.registerModule(module);

        return mapper;
    }
}
