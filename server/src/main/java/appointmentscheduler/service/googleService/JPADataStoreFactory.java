package appointmentscheduler.service.googleService;

import appointmentscheduler.repository.GoogleCredentialRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;


@org.springframework.stereotype.Service
public class JPADataStoreFactory extends AbstractDataStoreFactory {
    private GoogleCredentialRepository repository;

    @Autowired
    public JPADataStoreFactory(GoogleCredentialRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JPADataStoreService createDataStore(String id) throws IOException {
        return new JPADataStoreService(this, id, repository);
    }
}
