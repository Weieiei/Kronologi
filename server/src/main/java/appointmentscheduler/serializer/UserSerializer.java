package appointmentscheduler.serializer;

import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", user.getId());
        gen.writeStringField("firstName", user.getFirstName());
        gen.writeObjectField("lastName", user.getLastName());
        gen.writeObjectField("email", user.getEmail());
        gen.writeObjectField("roles", user.getRoles());

        if (user instanceof Employee) {
            gen.writeArrayFieldStart("services");
            ((Employee) user).getServices().forEach(service -> {
                try {
                    serializeService(service, gen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }

    private void serializeService(Service service, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", service.getId());
        gen.writeStringField("name", service.getName());
        gen.writeObjectField("duration", service.getDuration());
        gen.writeEndObject();
    }
}
