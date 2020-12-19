package si.fri.rso.albify.uploadimageservice.models.entities;

import org.bson.types.ObjectId;

import java.util.Date;

public class RecognitionRequestEntity {

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}