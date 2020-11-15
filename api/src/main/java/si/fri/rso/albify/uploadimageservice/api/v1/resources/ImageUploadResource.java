package si.fri.rso.albify.uploadimageservice.api.v1.resources;

import si.fri.rso.albify.uploadimageservice.lib.Image;
import si.fri.rso.albify.uploadimageservice.models.entities.ImageEntity;
import si.fri.rso.albify.uploadimageservice.services.beans.ImageBean;
import si.fri.rso.albify.uploadimageservice.services.beans.S3Bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
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

    @Context
    protected UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadImage(@HeaderParam("userId") String userId, InputStream uploadedInputStream) {
        System.out.println("S3 DATA");
        System.out.println(System.getenv());
        System.out.println(System.getenv("AWS_ACCESS_KEY_ID"));
        System.out.println(System.getenv("AWS_SECRET_KEY"));
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
