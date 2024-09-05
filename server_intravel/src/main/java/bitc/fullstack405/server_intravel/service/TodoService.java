package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.TodoEntity;
import bitc.fullstack405.server_intravel.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  public List<TodoEntity> listAll(Long travelId) {
    return todoRepository.findByTravelId(travelId);
  }

  public TodoEntity save(Long travelId, TodoEntity todoEntity) {
    todoEntity.setTravelId(travelId);
    return todoRepository.save(todoEntity);
  }

  @Transactional
  public TodoEntity update(Long todoId, TodoEntity todoEntity) {
    TodoEntity updateTodo = todoRepository.findByTodoIdAndTravelId(todoEntity.getTodoId(), todoId);

    updateTodo.setTdImpo(todoEntity.getTdImpo());
    updateTodo.setTdContent(todoEntity.getTdContent());

    return updateTodo;
  }

  public void deleteBytId(Long todoId) {
    todoRepository.deleteByTodoId(todoId);
  }

  public List<TodoEntity> listUncomp(Long travelId, char comp) {
//    char complete = 'N';
    return todoRepository.findByTravelIdAndTdComplete(travelId, comp);
  }

//  public List<TodoEntity> listComp(Long travelId) {
//    char complete = 'Y';
//    return todoRepository.findByTravelIdAndTdComplete(travelId, complete);
//  }
}
