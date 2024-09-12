package bitc.fullstack405.server_intravel.utils;

import bitc.fullstack405.server_intravel.entity.PhotoEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtil {

  public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path uploadPath = Paths.get(uploadDir);

    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    Path filePath = uploadPath.resolve(fileName);
    Files.copy(file.getInputStream(), filePath);

    return fileName;
  }

  public static boolean deleteFile(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      return file.delete();
    }
    return false;
  }
}
