package appointmentscheduler.serializer;

import appointmentscheduler.entity.service.Service;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class AdminAddServiceSerializer extends StdSerializer<Service>{

    public AdminAddServiceSerializer() {
        this(null);
    }

    public AdminAddServiceSerializer(Class<Service> t) {
        super(t);
    }

    @Override
    public void serialize(Service service, JsonGenerator generator, SerializerProvider serializer) throws IOException{
        generator.writeStartObject();

        generator.writeObjectField("id", service.getId());
        generator.writeObjectField("created_at", service.getCreatedAt());
        generator.writeObjectField("updated_at", service.getUpdatedAt());
        generator.writeObjectField("duration", service.getDuration());
        generator.writeObjectField("name", service.getName());
        generator.writeEndObject();

    }


}
