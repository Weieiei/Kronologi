package appointmentscheduler.serializer;

import appointmentscheduler.entity.event.AppEventBase;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AppEventBaseSerializer extends StdSerializer<AppEventBase> {

    public AppEventBaseSerializer() {
        this(null);
    }

    protected AppEventBaseSerializer(Class<AppEventBase> t) {
        super(t);
    }

    @Override
    public void serialize(AppEventBase appEventBase, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("date", appEventBase.getDate());
        gen.writeObjectField("sartTime", appEventBase.getStartTime());
        gen.writeObjectField("endTime", appEventBase.getEndTime());

        gen.writeEndObject();
    }
}
