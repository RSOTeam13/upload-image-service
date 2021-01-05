package si.fri.rso.albify.uploadimageservice.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.glassfish.jersey.server.ContainerRequest;
import si.fri.rso.albify.uploadimageservice.lib.Image;
import si.fri.rso.albify.uploadimageservice.models.entities.ImageEntity;
import si.fri.rso.albify.uploadimageservice.services.beans.ImageBean;
import si.fri.rso.albify.uploadimageservice.services.beans.RecognitionServiceBean;
import si.fri.rso.albify.uploadimageservice.services.beans.S3Bean;
import si.fri.rso.albify.uploadimageservice.services.filters.Authenticate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/upload")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(name = "image-upload-resource")
public class ImageUploadResource {

    private Logger log = Logger.getLogger(ImageUploadResource.class.getName());

    @Inject
    private S3Bean s3Bean;

    @Inject
    private ImageBean imageBean;

    @Inject
    private RecognitionServiceBean recognitionBean;

    @Context
    protected UriInfo uriInfo;

    @Metered(name = "upload_requests")
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Authenticate
    public Response uploadImage(InputStream uploadedInputStream, @Context ContainerRequest request) {
        if(request.getProperty("userId").toString() == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {

            byte[] bytes = uploadedInputStream.readAllBytes();
            String imageKey = s3Bean.uploadImage(bytes);
            Image newImage = new Image();
            newImage.setOwnerId(request.getProperty("userId").toString());
            newImage.setUrl(s3Bean.getUrl(imageKey).toString());
            newImage.setCreatedAt(new Date());
            String[] tags = new String[]{};
            try {
                tags = recognitionBean.getTags(imageKey).toArray(String[]::new);
            } catch (Exception e) {
                e.printStackTrace();
            }
            newImage.setTags(tags);
            ImageEntity resultEntity = imageBean.createImage(newImage);

            if(resultEntity != null){
                return Response.status(Response.Status.CREATED).build();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
