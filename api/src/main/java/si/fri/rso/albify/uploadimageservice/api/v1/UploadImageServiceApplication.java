package si.fri.rso.albify.uploadimageservice.api.v1;

import si.fri.rso.albify.uploadimageservice.api.v1.resources.DemoResource;
import si.fri.rso.albify.uploadimageservice.api.v1.resources.ImageUploadResource;
import si.fri.rso.albify.uploadimageservice.services.filters.AuthenticationFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/v1")
public class UploadImageServiceApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(DemoResource.class);
        resources.add(ImageUploadResource.class);
        resources.add(AuthenticationFilter.class);

        return resources;
    }

}
