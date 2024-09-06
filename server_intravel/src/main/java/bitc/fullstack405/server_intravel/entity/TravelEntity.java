package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Table(name = "travel")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long travId;

  private String travTitle;

  private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

  private String startDate;

  private String endDate;

//  private Long dDay;

  private char travComplete;

  private String cate;

//  @OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
//  private List<TodoEntity> todoEntityList;
//
//  @OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
//  private List<MemoEntity> memoEntityList;

}
