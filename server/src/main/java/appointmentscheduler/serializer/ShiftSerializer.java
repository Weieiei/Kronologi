package appointmentscheduler.serializer;

import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ShiftSerializer extends StdSerializer<Shift> {

    public ShiftSerializer() {
        this(null);
    }

    public ShiftSerializer(Class<Shift> t) {
        super(t);
    }

    @Override
    public void serialize(Shift shift, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", shift.getId());
        serializeEmployee(shift.getEmployee(), gen);
        gen.writeStringField("date", shift.getDate().toString());
        gen.writeStringField("startTime", shift.getStartTime().toString());
        gen.writeStringField("endTime", shift.getEndTime().toString());

        gen.writeEndObject();
    }

    private void serializeEmployee(Employee employee, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("employee");
        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeStringField("lastName", employee.getLastName());
        gen.writeEndObject();
    }
}
