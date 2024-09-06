package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

  List<TodoEntity> findByTravelId(Long travelId);

  TodoEntity findByTodoIdAndTravelId(Long todoId, Long travelId);

  List<TodoEntity> findByTravelIdAndTdComplete(Long travelId, char tdComplete);

  void deleteByTodoId(Long tdId);

  void deleteByTravelId(Long travelId);
}
