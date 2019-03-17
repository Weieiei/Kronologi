package appointmentscheduler.service.googleService;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.googleEntity.SyncEntity;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.SyncSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;


@org.springframework.stereotype.Service
public class GoogleSyncService {

    private SyncSettingRepository syncSettingRepository;

    @Autowired
    public GoogleSyncService(SyncSettingRepository syncSettingRepository) {
      this.syncSettingRepository = syncSettingRepository;
    }

    public void saveSyncToken(SyncEntity sync){
        syncSettingRepository.save(sync);
    }

    public SyncEntity findById(long user_id){
        return syncSettingRepository.findByUserId(user_id).get();
    }
}
