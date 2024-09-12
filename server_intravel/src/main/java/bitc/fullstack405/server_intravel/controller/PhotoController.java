package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.PhotoEntity;
import bitc.fullstack405.server_intravel.service.PhotoService;
import bitc.fullstack405.server_intravel.utils.FileUtil;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

  private final PhotoService photoService;
  private final FileUtil fileUtil;

  @GetMapping("/list/{tId}")
  public List<PhotoEntity> photoList(@PathVariable("tId") Long travId) {
    return photoService.photoList(travId);
  }

  @PostMapping("/save/{tId}")
  public PhotoEntity savePhoto(@PathVariable("tId") Long travId, @RequestParam("photo") MultipartFile file) throws IOException {
    return photoService.savePhoto(file, travId);
  }

//  수정, 필요없을 시 삭제
  @PutMapping("/update/{photoId}")
  public PhotoEntity updatePhoto(@PathVariable("photoId") Long photoId, @RequestBody PhotoEntity photoEntity) {
    return photoService.updatePhoto(photoId, photoEntity);
  }

  @DeleteMapping("/delete/{photoId}")
  public void deletePhoto(@PathVariable("photoId") Long photoId) {
    photoService.deletePhoto(photoId);
  }
}
