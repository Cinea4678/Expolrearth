package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.service.UploadService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.spencerwi.either.Either;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author cinea
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Value("${app.s3.bucket}")
    String bucket;

    @Value("${app.s3.webPrefix}")
    String webPrefix;

    @Value("${app.s3.path.image-upload}")
    String imageUploadPath;

    AmazonS3 amazonS3Client;

    public UploadServiceImpl(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public Either<String, String> uploadImage(InputStream inputStream, String fileName, Long fileSize, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(fileSize);

        var extensionName = fileName.substring(fileName.lastIndexOf('.'));
        var timestamp = System.currentTimeMillis();
        var rand = (int) (Math.random() * 10000);
        var key = String.format("%s/%d-%d%s", imageUploadPath, timestamp, rand, extensionName);


        amazonS3Client.putObject(bucket, key, inputStream, metadata);
        return Either.left(String.format("%s/%s", webPrefix, key));
    }
}
