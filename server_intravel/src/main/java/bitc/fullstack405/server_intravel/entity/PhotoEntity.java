package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Table(name = "photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PhotoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long photoId;

  private Long travId;

  private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

  private String filePath;

  private String fileName;
}
