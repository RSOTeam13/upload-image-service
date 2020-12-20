package si.fri.rso.albify.uploadimageservice.services.beans;

import org.glassfish.jersey.server.ContainerRequest;
import si.fri.rso.albify.uploadimageservice.models.entities.RecognitionRequestEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
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
    }


    /**
     * Calls recognition service and returns list of tags.
     * @return List of images.
     */
    public List<String> getTags(String imagePath) {
        String url = baseUrl + "/recognition";
        RecognitionRequestEntity entity = new RecognitionRequestEntity();
        entity.setImagePath(imagePath);
        return httpClient
                .target(url)
                .request()
                .post(Entity.json(entity), new GenericType<List<String>>() {});
    }

}