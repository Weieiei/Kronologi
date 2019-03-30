package appointmentscheduler.service.file;

import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.file.UserFile;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.repository.UserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserFileStorageService {
    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    UserRepository userRepository;


    public UserFileStorageService(UserFileRepository userFileRepository, UserRepository userRepository) {
        this.userFileRepository = userFileRepository;
        this.userRepository = userRepository;
    }

    public static boolean verifyNaming(String fileName) {
        //alphanumeric character followed by dot and alpha character
        final Pattern pattern = Pattern.compile("\\w*.[A-Za-z]*");
        return pattern.matcher(fileName).matches();
    }

    public Map<String, String> saveUserFile(MultipartFile file, long userId) {
        // clean file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        User user = userRepository.findById(userId).get();

        try {
            // Check if the file's name contains invalid characters
            if(!verifyNaming(fileName)) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String message;
            UserFile newFile = new UserFile(fileName, file.getContentType(), file.getBytes(),user);
            userFileRepository.save(newFile);
            return message("You've successfully updated your profile.");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional
    public UserFile getFile(long fileId, long userId) {
        return userFileRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
