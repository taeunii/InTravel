package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.*;

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

//  @ManyToOne
//  private TravelEntity travel;
}
