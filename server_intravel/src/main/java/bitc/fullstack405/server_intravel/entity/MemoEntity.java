package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Table(name = "memo")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memoId;

  private Long travId;

  private String memoTitle;

  private String memoContent;

  private String choiceDate;

  private String memoCreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

//  @ManyToOne
//  private TravelEntity travel;
}
