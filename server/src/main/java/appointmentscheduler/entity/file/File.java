package appointmentscheduler.entity.file;
import appointmentscheduler.entity.business.Business;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Column(name = "fileOrigin")
    private FileOrigin fileOrigin;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    private long size;

    @Lob
    private byte[] data;

    public File() {
        this.fileOrigin = FileOrigin.MISC;
    }

    public File(String fileName, String fileType, byte[] data, Business business) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = FileOrigin.MISC;
        this.business = business;
    }

    public File(String fileName, String fileType, byte[] data, FileOrigin fileOrigin, Business business) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = fileOrigin;
        this.business = business;
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

    public String getId() {
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

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
