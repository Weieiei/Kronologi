package appointmentscheduler.service.file;

import appointmentscheduler.entity.file.ServiceFile;

import appointmentscheduler.entity.service.Service;

import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.ServiceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import appointmentscheduler.util.StringMatcher;

@org.springframework.stereotype.Service
public class ServiceFileStorageService {
    @Autowired
    private ServiceFileRepository serviceFileRepository;

    @Autowired
    ServiceRepository serviceRepository;


    public ServiceFileStorageService(ServiceFileRepository serviceFileRepository, ServiceRepository serviceRepository) {
        this.serviceFileRepository = serviceFileRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public Map<String, String> saveServiceFile(MultipartFile file, long serviceId) {
        // clean file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Service service = serviceRepository.findById(serviceId).get();

        try {
            // Check if the file's name contains invalid characters
            if(!StringMatcher.verifyNaming(fileName)) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            ServiceFile newFile = new ServiceFile(fileName, file.getContentType(), file.getBytes(),service);

            if (serviceFileRepository.findByServiceId(serviceId).isPresent()) {
                long id = serviceFileRepository.findByServiceId(serviceId).get().getId();
                serviceFileRepository.deleteById(id);

            }

            serviceFileRepository.save(newFile);


            return message("You've successfully updated the profile of the service.");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional
    public ServiceFile getFile(long fileId, long serviceId) {
        return serviceFileRepository.findByIdAndServiceId(fileId, serviceId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }

    @Transactional
    public ServiceFile getServiceFile(long serviceId) {
        return serviceFileRepository.findByServiceId(serviceId)
                .orElseThrow(() -> new FileStorageException("File not found"));
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
