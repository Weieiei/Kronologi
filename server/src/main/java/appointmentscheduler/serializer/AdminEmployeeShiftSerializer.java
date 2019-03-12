package appointmentscheduler.serializer;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AdminEmployeeShiftSerializer extends StdSerializer<Shift> {

    public AdminEmployeeShiftSerializer() {
        this(null);
    }

    public AdminEmployeeShiftSerializer(Class<Shift> t) {
        super(t);
    }

    @Override
    public void serialize(Shift shift, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", shift.getId());
        serializeBusiness(shift.getBusiness(), gen);
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
        gen.writeStringField("email", employee.getEmail());
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
