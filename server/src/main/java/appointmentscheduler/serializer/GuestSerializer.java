package appointmentscheduler.serializer;

import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GuestSerializer extends StdSerializer<User> {

    public GuestSerializer() {
        this(null);
    }

    public GuestSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User guest, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", guest.getId());
        gen.writeStringField("firstName", guest.getFirstName());
        gen.writeObjectField("lastName", guest.getLastName());
        gen.writeObjectField("email", guest.getEmail());
        gen.writeObjectField("roles", guest.getRole());

        gen.writeEndObject();
    }
}
