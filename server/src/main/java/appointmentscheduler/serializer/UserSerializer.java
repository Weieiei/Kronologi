package appointmentscheduler.serializer;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


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
        gen.writeObjectField("roles", user.getRole());
        serializeBusiness(user.getBusiness(), gen);

        if (user instanceof Employee) {
            gen.writeArrayFieldStart("services");
            ((Employee) user).getServices().forEach(service -> {
                try {
                    serializeService(service.getService(), gen);
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

    public void serializeBusiness(Business business, JsonGenerator gen) throws IOException {

        gen.writeObjectFieldStart("business");
        gen.writeObjectField("id", business.getId());
        gen.writeStringField("name", business.getName());
        gen.writeObjectField("domain", business.getDomain());
        gen.writeObjectField("description", business.getDescription());

        gen.writeEndObject();

    }
}
