package si.fri.rso.albify.uploadimageservice.services.beans;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.glassfish.jersey.server.ContainerRequest;
import si.fri.rso.albify.uploadimageservice.models.entities.RecognitionRequestEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class RecognitionServiceBean {

    @Inject
    ContainerRequest request;

    private Client httpClient;
    private String baseUrl;

    private Logger log = Logger.getLogger(RecognitionServiceBean.class.getName());

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = "http://image-recognition-service:8080/v1";
//        baseUrl = "http://localhost:8081/v1";
        System.out.println("Init with baseUrl: " + baseUrl);
    }


    /**
     * Calls recognition service and returns list of tags.
     * @return List of images.
     */
    @Fallback(fallbackMethod = "getTagsFallback")
    public List<String> getTags(String imagePath, Boolean forceFail) throws Exception {
        log.info("Getting tags");
        if (forceFail) {
            log.info("Force failing for tags");
            throw new Exception("Tag list forced fail");
        }
        String url = baseUrl + "/recognition";
        System.out.println("Get tags over url: " + url);
        System.out.println("Image path: " + imagePath);
        RecognitionRequestEntity entity = new RecognitionRequestEntity();
        entity.setImagePath(imagePath);
        return httpClient
                .target(url)
                .request()
                .post(Entity.json(entity), new GenericType<List<String>>() {});
    }
    public List<String> getTags(String imagePath) throws Exception {
        return this.getTags(imagePath, false);
    }

    public List<String> getTagsFallback(String imagePath, Boolean forceFail) throws Exception {
        return new ArrayList<>(){};
    }


}