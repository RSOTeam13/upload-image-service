package si.fri.rso.albify.uploadimageservice.models.entities;

import org.bson.types.ObjectId;
import java.util.Date;

public class ImageEntity {

    private ObjectId id;
    private Date createdAt;
    private ObjectId ownerId;
    private String url;
    private String[] tags;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] url) {
        this.tags = tags;
    }
}