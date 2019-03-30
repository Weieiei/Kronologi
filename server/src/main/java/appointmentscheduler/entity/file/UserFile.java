package appointmentscheduler.entity.file;
import appointmentscheduler.entity.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "userFiles")
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Column(name = "fileOrigin")
    private FileOrigin fileOrigin;


    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    private long size;

    @Lob
    private byte[] data;

    public UserFile() {
        this.fileOrigin = FileOrigin.MISC;
    }

    public UserFile(String fileName, String fileType, byte[] data, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = FileOrigin.MISC;
        this.user = user;
    }

    public UserFile(String fileName, String fileType, byte[] data, FileOrigin fileOrigin, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.fileOrigin = fileOrigin;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
