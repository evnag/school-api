package ru.hogwarts.schoolapi.record;

public class AvatarRecord {
    private Long id;
    private String filePath;
    private String mediaType;

    // default ctor for jackson
    public AvatarRecord() {
    }

    public AvatarRecord(Long id, String filePath, String mediaType) {
        this.id = id;
        this.filePath = filePath;
        this.mediaType = mediaType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
