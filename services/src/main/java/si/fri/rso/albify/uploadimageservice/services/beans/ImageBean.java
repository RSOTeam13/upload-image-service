package si.fri.rso.albify.uploadimageservice.services.beans;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import si.fri.rso.albify.uploadimageservice.lib.Image;
import si.fri.rso.albify.uploadimageservice.models.converters.ImageConverter;
import si.fri.rso.albify.uploadimageservice.models.entities.ImageEntity;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@RequestScoped
public class ImageBean {

    private Logger log = Logger.getLogger(ImageBean.class.getName());

    private CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    private MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(System.getenv("DB_URL")))
            .codecRegistry(pojoCodecRegistry)
            .build();


    private MongoClient mongoClient = MongoClients.create(settings);
    private MongoDatabase database = mongoClient.getDatabase("albify");
    private MongoCollection<ImageEntity> imagesCollection = database.getCollection("images", ImageEntity.class);

    @PreDestroy
    private void onDestroy() {
        try {
            mongoClient.close();
        } catch (Exception e) {
            log.severe("Error when closing image bean database connection.");
            e.printStackTrace();
        }
    }

    /**
     * Returns image by its ID.
     * @param imageId Image ID.
     * @param userId User ID.
     * @return whether the provided image belongs to the provided user.
     */
    public boolean isOwner(String imageId, String userId) {
        try {
            ImageEntity entity = imagesCollection.find(
                    and(
                            eq("_id", new ObjectId(imageId)),
                            eq("ownerId", new ObjectId(userId))
                    )
            ).first();
            if (entity != null && entity.getId() != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns image by its ID.
     * @param imageId Image ID.
     * @return Image entity.
     */
    public ImageEntity getImage(String imageId) {
        try {
            ImageEntity entity = imagesCollection.find(eq("_id", new ObjectId(imageId))).first();
            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Creates new image.
     * @param image to create.
     * @return Newly created image.
     */
    @CircuitBreaker(requestVolumeThreshold = 2, delay = 10000)
    public ImageEntity createImage(Image image, Boolean forceFail) throws Exception {
        log.info("Creating image");
        if (forceFail) {
            log.info("Force failing for image");
            throw new Exception("Image create forced fail");
        }
        try {
            ImageEntity entity = ImageConverter.toEntity(image);
            entity.setCreatedAt(new Date());
            entity.setOwnerId(new ObjectId(image.getOwnerId()));
            entity.setUrl(image.getUrl());
            entity.setTags(new ArrayList<String>(Arrays.asList(image.getTags())));

            InsertOneResult result = imagesCollection.insertOne(entity);
            if (result != null) {
                return entity;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes image.
     * @param imageId Image ID.
     * @return Deleted album.
     */
    public ImageEntity removeImage(String imageId) {
        try {
            ImageEntity entity = imagesCollection.findOneAndDelete(eq("_id", new ObjectId(imageId)));
            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
