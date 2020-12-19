package si.fri.rso.albify.uploadimageservice.api.v1.resources;

import org.eclipse.microprofile.metrics.annotation.Metered;
import si.fri.rso.albify.uploadimageservice.lib.Image;
import si.fri.rso.albify.uploadimageservice.models.entities.ImageEntity;
import si.fri.rso.albify.uploadimageservice.services.beans.ImageBean;
import si.fri.rso.albify.uploadimageservice.services.beans.RecognitionServiceBean;
import si.fri.rso.albify.uploadimageservice.services.beans.S3Bean;

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
    public Response uploadImage(@HeaderParam("userId") String userId, InputStream uploadedInputStream) {
        if(userId == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {

            byte[] bytes = uploadedInputStream.readAllBytes();
            String imageKey = s3Bean.uploadImage(bytes);
            Image newImage = new Image();
            newImage.setOwnerId(userId);
            newImage.setUrl(s3Bean.getUrl(imageKey).toString());
            newImage.setCreatedAt(new Date());
            String[] tags = new String[]{};
            try {
                tags = (String[]) recognitionBean.getTags(imageKey).toArray();
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
