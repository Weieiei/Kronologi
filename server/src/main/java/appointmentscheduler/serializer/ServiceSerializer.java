package appointmentscheduler.serializer;

import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Set;

public class ServiceSerializer extends StdSerializer<Service> {

    public ServiceSerializer() {
        this(null);
    }

    public ServiceSerializer(Class<Service> t) {
        super(t);
    }

    @Override
    public void serialize(Service service, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", service.getId());
        gen.writeStringField("name", service.getName());
        gen.writeObjectField("duration", service.getDuration());
       //serializeEmployees(service.getEmployees(), gen);

        gen.writeEndObject();
    }

    private void serializeEmployees(Set<EmployeeService> employees, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("employees");

        for (EmployeeService emp_service : employees) {
            serializeEmployee(emp_service.getEmployee(), gen);
        }

        gen.writeEndArray();
    }

    private void serializeEmployee(Employee employee, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeStringField("lastName", employee.getLastName());
        gen.writeEndObject();
    }
}
