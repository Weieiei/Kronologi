package appointmentscheduler.service.file;

import appointmentscheduler.entity.file.File;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {
    @Autowired
    private FileRepository fileRepository;

    public FileStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public  File saveFile(MultipartFile file) {
        // clean file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File newFile = new File(fileName, file.getContentType(), file.getBytes());

            return fileRepository.save(newFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public File getFile(String fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }
}
