package appointmentscheduler.service.googleService;

import appointmentscheduler.entity.verification.GoogleCred;
import appointmentscheduler.repository.GoogleCredentialRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class JPADataStoreService<V extends Serializable> extends AbstractDataStore<StoredCredential> {

    @Autowired
    private GoogleCredentialRepository repository;

    @Autowired
    private JPADataStoreFactory jpaDataStoreFactory;

    /**
     * @param dataStoreFactory data store factory
     * @param id               data store ID
     */
    protected JPADataStoreService(JPADataStoreFactory dataStoreFactory, String id, GoogleCredentialRepository repository) {
        super(dataStoreFactory, id);
        this.repository = repository;
    }

    @Override
    public JPADataStoreFactory getDataStoreFactory() {
        return jpaDataStoreFactory;
    }

    @Override
    public int size() throws IOException {
        return (int) repository.count();
    }

    @Override
    public boolean isEmpty() throws IOException {
        return size() == 0;
    }

    @Override
    public boolean containsKey(String key) throws IOException {
        return repository.findByKey(key).isPresent();
    }

    @Override
    public boolean containsValue(StoredCredential value) throws IOException {
        return repository.findByAccessToken(value.getAccessToken()).isPresent();
    }

    @Override
    public Set<String> keySet() throws IOException {
        return repository.findAllKeys();
    }

    @Override
    public Collection<StoredCredential> values() throws IOException {
        return repository.findAll().stream().map(c -> {
            StoredCredential credential = new StoredCredential();
            credential.setAccessToken(c.getAccessToken());
            credential.setRefreshToken(c.getRefreshToken());
            credential.setExpirationTimeMilliseconds(c.getExpirationTimeMilliseconds());
            return credential;
        }).collect(Collectors.toList());
    }

    @Override
    public StoredCredential get(String key) throws IOException {
        Optional<GoogleCred> jpaStoredCredentialOptional = repository.findByKey(key);
        if (!jpaStoredCredentialOptional.isPresent()) {
            return null;
        }
        GoogleCred googleCredential = jpaStoredCredentialOptional.get();
        StoredCredential credential = new StoredCredential();
        credential.setAccessToken(googleCredential.getAccessToken());
        credential.setRefreshToken(googleCredential.getRefreshToken());
        credential.setExpirationTimeMilliseconds(googleCredential.getExpirationTimeMilliseconds());
        return credential;
    }

    @Override
    public DataStore<StoredCredential> set(String key, StoredCredential value) throws IOException {
        GoogleCred googleCredential = repository.findByKey(key).orElse(new GoogleCred(key, value));
        googleCredential.apply(value);
        repository.save(googleCredential);
        return this;
    }

    @Override
    public DataStore<StoredCredential> clear() throws IOException {
        repository.deleteAll();
        return this;
    }

    @Override
    public DataStore<StoredCredential> delete(String key) throws IOException {
        Optional<GoogleCred> jpaStoredCredentialOptional = repository.findByKey(key);
        if (!jpaStoredCredentialOptional.isPresent()) {
            return null;
        }
        repository.delete(jpaStoredCredentialOptional.get());
        return this;
    }


}
