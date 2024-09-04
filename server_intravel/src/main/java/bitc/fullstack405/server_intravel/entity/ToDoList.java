package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "to_do_list")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tdId;

  private Long tId;

  private String tdContent;

  private char tdComplete;

  private char tdImpo;
}
