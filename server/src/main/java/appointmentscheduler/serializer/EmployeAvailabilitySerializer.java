package appointmentscheduler.serializer;

import appointmentscheduler.entity.event.AppEventBase;
import appointmentscheduler.entity.user.EmployeeAvailability;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Set;

public class EmployeAvailabilitySerializer extends StdSerializer<EmployeeAvailability> {

    public EmployeAvailabilitySerializer() {
        this(null);
    }


    public EmployeAvailabilitySerializer(Class<EmployeeAvailability> t) {
        super(t);
    }

    @Override
    public void serialize(EmployeeAvailability employeeAvailability, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("employee", employeeAvailability.getEmployee().getFirstName());
        gen.writeObjectField("employee_id", employeeAvailability.getEmployee().getId());
        gen.writeObjectField("availabilities", employeeAvailability.getAvailabilities());

        gen.writeEndObject();
    }
}
