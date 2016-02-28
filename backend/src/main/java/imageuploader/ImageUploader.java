package imageuploader;

import java.io.InputStream;

public interface ImageUploader {

  String uploadImage(InputStream inputStream);
}
