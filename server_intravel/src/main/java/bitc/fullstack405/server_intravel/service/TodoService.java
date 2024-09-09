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

  public List<TodoEntity> listAll(Long travId) {
    return todoRepository.findByTravId(travId);
  }

  public TodoEntity save(Long travId, TodoEntity todoEntity) {
    todoEntity.setTravId(travId);
    return todoRepository.save(todoEntity);
  }

  @Transactional
  public TodoEntity update(Long todoId, TodoEntity todoEntity) {
    TodoEntity updateTodo = todoRepository.findById(todoId).get();

    updateTodo.setTravId(todoEntity.getTravId());
    updateTodo.setTodoComplete(todoEntity.getTodoComplete());
    updateTodo.setTodoImpo(todoEntity.getTodoImpo());
    updateTodo.setTodoContent(todoEntity.getTodoContent());

    return updateTodo;
  }

  @Transactional
  public void deleteByTodoId(Long todoId) {
    todoRepository.deleteByTodoId(todoId);
  }

//  public List<TodoEntity> listUncomp(Long travelId) {
//    char complete = 'N';
//    return todoRepository.findByTravIdAndTodoComplete(travelId, complete);
//  }

  public List<TodoEntity> listIsComplete(Long travelId, char todoComplete) {
    return todoRepository.findByTravIdAndTodoComplete(travelId, todoComplete);
  }

//  public List<TodoEntity> listUncomp(Long travelId, char comp) {
////    char complete = 'N';
//    return todoRepository.findByTravelIdAndTdComplete(travelId, comp);
//  }

//  public List<TodoEntity> listComp(Long travelId) {
//    char complete = 'Y';
//    return todoRepository.findByTravelIdAndTdComplete(travelId, complete);
//  }
}
