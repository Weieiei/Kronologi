package appointmentscheduler.entity.room_service;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.service.Service;

import javax.persistence.*;

@Entity
@Table(name = "room_service_entity")
@IdClass(RoomServiceEntity.class)
public class RoomServiceEntity {

    @Id
    private long roomId;

    @Id
    private long serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ROOMID", referencedColumnName="id")
    private Room room;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="SERVICEID", referencedColumnName="id")
    private Service service;
}

