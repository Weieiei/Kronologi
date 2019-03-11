package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/${rest.api.path}/", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    private final BusinessService businessService;

    public FileController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @LogREST
    @PostMapping("/business/{businessId}/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile aFile, @PathVariable long businessId) {
        File file = fileStorageService.saveFile(aFile, businessId);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/business/")
                .path(String.valueOf(businessId))
                .path("/downloadFile/")
                .path(String.valueOf(file.getId()))
                .toUriString();

        return fileDownloadUri;
    }

    @LogREST
    @GetMapping("/business/{businessId}/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long businessId, @PathVariable long fileId) {
        // Load file from database
        File file = fileStorageService.getFile(fileId, businessId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }
}
