package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  private char tComplete;

  private String cate;
}
