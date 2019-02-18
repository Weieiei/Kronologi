package appointmentscheduler.serializer;

import appointmentscheduler.entity.user.Employee;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AdminEmployeeSerializer extends StdSerializer<Employee> {

    public AdminEmployeeSerializer() {
        this(null);
    }

    public AdminEmployeeSerializer(Class<Employee> t) {
        super(t);
    }

    @Override
    public void serialize(Employee employee, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeObjectField("lastName", employee.getLastName());
        gen.writeObjectField("email", employee.getEmail());

        gen.writeEndObject();
    }
}
