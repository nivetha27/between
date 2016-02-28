package imageuploader;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import core.exceptions.EntityTooLargeException;

import java.io.InputStream;
import java.util.UUID;

public class S3ImageUploader implements ImageUploader {

  final private AmazonS3 amazonS3;

  final private String bucketName;

  public S3ImageUploader(AmazonS3 amazonS3, String bucketName) {
    this.amazonS3 = amazonS3;
    this.bucketName = bucketName;
  }

  @Override
  public String uploadImage(InputStream inputStream) {
    String id = UUID.randomUUID().toString();
    try {
      PutObjectRequest request = new PutObjectRequest(bucketName, id, inputStream, new ObjectMetadata());
      amazonS3.putObject(request);
    } catch (AmazonServiceException e) {
      if (e.getErrorCode().equalsIgnoreCase("EntityTooLarge")) {
        throw new EntityTooLargeException("Image size is too large");
      }
      throw e;
    }
    return id;
  }
}
