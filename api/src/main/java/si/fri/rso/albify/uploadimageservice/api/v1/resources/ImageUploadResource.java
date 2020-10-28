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

    @GET
    @Path("{imageId}")
    public Response getImage(@PathParam("imageId") String imageId, @HeaderParam("userId") String userId){
        if(userId == null){
            userId = "public";
        }
        // TODO: check sharing?
        if(!imageBean.isOwner(imageId, userId)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(!s3Bean.imageExists(imageId)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try{
            URI uri = s3Bean.getUrl(imageId);
            return Response.seeOther(uri).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response uploadImage(String image, @HeaderParam("userId") String userId) {
        System.out.println(System.getenv("AWS_ACCESS_KEY_ID"));
        if(userId == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {

            byte[] processed = Base64.getDecoder().decode(image);
            String imageKey = s3Bean.uploadImage(processed);
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

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("imageId") String imageId, @HeaderParam("userId") String userId) {
        if(userId == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if(!imageBean.isOwner(imageId, userId)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(!s3Bean.imageExists(imageId)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
//            Database.RemoveImage(imageId, userId);
            imageBean.removeImage(imageId);
            s3Bean.deleteImage(imageId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

}
