package si.fri.rso.albify.uploadimageservice.api.v1.resources;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.metrics.annotation.Metered;
import si.fri.rso.albify.uploadimageservice.lib.DemoData;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private S3Bean s3Bean;

    @Inject
    private ImageBean imageBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getData() {

        DemoData data = new DemoData();

        String[] clani = new String[]{"jb6407", "am3158"};
        String opis = "Aplikacija za upravljanje s slikami.";
        String[] mikrostoritve = new String[]{"TODO"};
        String[] github = new String[]{"https://github.com/RSOTeam13/upload-image-service", "https://github.com/RSOTeam13/albums-service", "https://github.com/RSOTeam13/images-service", "https://github.com/RSOTeam13/users-service"};
        String[] travis = new String[]{"TODO"};
        String[] dockerhub = new String[]{"https://hub.docker.com/r/anzemur/upload-image-service-api", "https://hub.docker.com/r/anzemur/album-service-api", "https://hub.docker.com/r/anzemur/image-service-api", "https://hub.docker.com/r/anzemur/users-service-api"};


        data.setClani(clani);
        data.setOpis_projekta(opis);
        data.setMikrostoritve(mikrostoritve);
        data.setGithub(github);
        data.setTravis(travis);
        data.setDockerhub(dockerhub);

        return Response.status(Response.Status.OK)
                .entity(data)
                .build();
    }
}
