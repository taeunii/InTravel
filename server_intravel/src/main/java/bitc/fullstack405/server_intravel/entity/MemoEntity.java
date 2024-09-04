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
  private Long mId;

  private Long tId;

  private String mTitle;

  private String mContent;
}
