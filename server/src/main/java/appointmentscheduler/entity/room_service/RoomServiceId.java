package appointmentscheduler.entity.room_service;

import java.io.Serializable;

public class RoomServiceId implements Serializable {

    private long roomId;

    private long serviceId;

    public int hashCode() {
        return (int)(roomId + serviceId);
    }

    public boolean equals(Object object) {
        if (object instanceof RoomServiceId) {
            RoomServiceId otherId = (RoomServiceId) object;
            return (otherId.roomId == this.roomId) && (otherId.serviceId == this.serviceId);
        }
        return false;
    }
}
