package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "travel")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tId;

  private String tTitle;

  private LocalDateTime createDate = LocalDateTime.now();

  private String startDate;

  private String endDate;

//  private Long dDay;

  private char tComplete = 'N';

  private String cate;

}
