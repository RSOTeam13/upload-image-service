package si.fri.rso.albify.uploadimageservice.services.beans;

import javax.enterprise.context.RequestScoped;
import java.util.logging.Logger;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;

@RequestScoped
public class S3Bean {

    private Logger log = Logger.getLogger(S3Bean.class.getName());


    public static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new EnvironmentVariableCredentialsProvider()).build();
    public static final String bucketName = "albify"; // TODO: pass in via configuration

    public static String uploadImage(byte[] image) throws Exception{
        if (image == null) throw new IllegalArgumentException("file stream is not image");
        InputStream imageStream = new ByteArrayInputStream(image);
        String mime = URLConnection.guessContentTypeFromStream(imageStream);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mime);
        metadata.setContentLength(image.length);
        String objectName = Long.toString(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(bucketName, objectName, imageStream, metadata);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
        return s3.getObject(bucketName, objectName).getKey();
    }

    public static URI getUrl(String objectKey) throws Exception {
        return s3.getUrl(bucketName, objectKey).toURI();
    }

    public static void deleteImage(String objectKey) {
        s3.deleteObject(bucketName, objectKey);
    }

    public static boolean imageExists(String objectKey) {
        return s3.doesObjectExist(bucketName, objectKey);
    }

}
