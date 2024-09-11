package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

  List<TodoEntity> findByTravId(Long travId);

  TodoEntity findByTodoIdAndTravId(Long todoId, Long travId);

  List<TodoEntity> findByTravIdAndTodoComplete(Long travId, char todoComplete);

  void deleteByTodoId(Long todoId);

  void deleteByTravId(Long travId);
}
