package appointmentscheduler.entity.file;
import appointmentscheduler.entity.service.Service;

import javax.persistence.*;

@Entity
@Table(name = "service_files")
public class ServiceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Column(name = "file_origin")
    private FileOrigin fileOrigin;


    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    private Service service;

    private long size;

    @Lob
    private byte[] data;

    public ServiceFile() {
        this.fileOrigin = FileOrigin.SERVICE;
    }

    public ServiceFile(String fileName, String fileType, byte[] data, Service service) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = FileOrigin.SERVICE;
        this.service = service;
    }

    public ServiceFile(String fileName, String fileType, byte[] data, FileOrigin fileOrigin, Service service) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = fileOrigin;
        this.service = service;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public long getId() {
        return id;
    }

    public long getSize() {
        return  size;
    }


    public FileOrigin getFileOrigin() {
        return fileOrigin;
    }

    public void setFileOrigin(FileOrigin fileOrigin) {
        this.fileOrigin = fileOrigin;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }



}
