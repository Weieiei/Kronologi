package appointmentscheduler.serializer;

import appointmentscheduler.entity.business.Business;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class BusinessSerializer extends StdSerializer<Business> {

    public BusinessSerializer() {
        this(null);
    }

    public BusinessSerializer(Class<Business> t) {
        super(t);
    }

    @Override
    public void serialize(Business business, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        gen.writeStartObject();

        gen.writeObjectField("id", business.getId());
        gen.writeStringField("name", business.getName());
        gen.writeObjectField("domain", business.getDomain());
        gen.writeObjectField("description", business.getDescription());

        gen.writeEndObject();

    }
}
