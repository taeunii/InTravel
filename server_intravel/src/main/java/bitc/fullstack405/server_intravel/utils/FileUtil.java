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

//  public PhotoEntity uploadFile(PhotoEntity photoEntity, File file) throws IOException {
//
//    String fileName = photoEntity.getFileName();
//    photoEntity.setFileName(fileName);
//
//    String fileNameExtension = fileName + ".jpg";
//
//    String filePath = getSaveFilePath();
//
//    File f = new File(filePath);
//
//    if (!fileName.isEmpty()) {
//      f.createNewFile();
//
//      photoEntity.setFileName(fileNameExtension);
//      return photoEntity;
//    }
//    else {
//      return null;
//    }
//  }

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

  public void deleteFile(PhotoEntity photoEntity) {
    File file = new File(getSaveFilePath() + File.separator + photoEntity.getFileName());

    if (file.exists()) {
      file.delete();
    }
  }

  public String getSaveFilePath() {
    File rootPath = new File("");
    String savePath = rootPath.getAbsolutePath();
    int subStdIdx = savePath.lastIndexOf("\\");
    savePath = savePath.substring(0, subStdIdx) + "\\AndroidImages\\";

    return savePath;
  }

}
