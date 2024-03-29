package appointmentscheduler.service.file;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.FileRepository;
import appointmentscheduler.util.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FileStorageService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    BusinessRepository businessRepository;


    public FileStorageService(FileRepository fileRepository, BusinessRepository businessRepository) {
        this.fileRepository = fileRepository;
        this.businessRepository = businessRepository;
    }


    public  File saveFile(MultipartFile file, long businessId) {
        // clean file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Business myBusiness = businessRepository.findById(businessId).get();

        try {
            // Check if the file's name contains invalid characters
            if(!StringMatcher.verifyNaming(fileName)) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File newFile = new File(fileName, file.getContentType(), file.getBytes(),myBusiness);

            return fileRepository.save(newFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional
    public File getFile(long fileId, long businessId) {
        return fileRepository.findByIdAndBusinessId(fileId, businessId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }
}
