package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.PhotoEntity;
import bitc.fullstack405.server_intravel.repository.PhotoRepository;
import bitc.fullstack405.server_intravel.utils.FileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {

  private final PhotoRepository photoRepository;

  @Value("${file.upload-dir}")
  private String uploadDir;


  public List<PhotoEntity> photoList(Long travId) {
    return photoRepository.findByTravId(travId);
  }

//  @Transactional
//  public PhotoEntity save(Long travId, PhotoEntity photoEntity) {
//    photoEntity.setTravId(travId);
//    return photoRepository.save(photoEntity);
//  }



  public PhotoEntity savePhoto(MultipartFile file, Long travId) throws IOException {
    String fileName = FileUtil.saveFile(file, uploadDir);
    String filepath = uploadDir + File.separator;

    PhotoEntity photo = new PhotoEntity();
    photo.setTravId(travId);
    photo.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    photo.setFilePath(filepath);
    photo.setFileName(fileName);

    return photoRepository.save(photo);
  }


  @Transactional
  public PhotoEntity updatePhoto(Long photoId, PhotoEntity photoEntity) {
    PhotoEntity updatePhoto = photoRepository.findById(photoId).get();

//    추가할 내용 필요시 입력

    return updatePhoto;
  }

  @Transactional
  public void deletePhoto(Long photoId) {
    PhotoEntity photo = photoRepository.findById(photoId).get();

    String fullPath = photo.getFilePath() + photo.getFileName();

    boolean isFileDeleted = FileUtil.deleteFile(fullPath);
    if (!isFileDeleted) {
      throw new IllegalStateException("Failed to delete file: " + fullPath);
    }

    photoRepository.deleteById(photoId);
  }
}
