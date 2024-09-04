package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
