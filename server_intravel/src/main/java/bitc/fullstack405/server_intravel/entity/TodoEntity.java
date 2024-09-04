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

  private Long tId;

  private String tdContent;

<<<<<<< HEAD:server_intravel/src/main/java/bitc/fullstack405/server_intravel/entity/TodoEntity.java
  private char tdComplete = 'N';
=======
  private char tdComplete;
>>>>>>> origin/jun/back:server_intravel/src/main/java/bitc/fullstack405/server_intravel/entity/ToDoList.java

  private char tdImpo;
}
