package appointmentscheduler.entity.file;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    private String contentType;

    private long size;

    @Lob
    private byte[] data;

    public File() {

    }

    public File(String fileName, String fileType, byte[] data, String contentType) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        size = data.length;
        this.contentType = contentType;
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

    public String getContentType() {
        return contentType;
    }
}
