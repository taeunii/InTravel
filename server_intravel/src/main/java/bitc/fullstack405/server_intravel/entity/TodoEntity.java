package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "to_do_list")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long todoId;

  private Long travelId;

  private String tdContent;

  private char tdComplete = 'N';

  private char tdImpo;
}
