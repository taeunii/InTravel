package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.TodoEntity;
import bitc.fullstack405.server_intravel.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

//  To do list 전체보기
  @GetMapping("/listAll/{tId}")
  public List<TodoEntity> listAll(@PathVariable("tId") Long travId) {
    return todoService.listAll(travId);
  }

//  To do list 완료/미완료 보기
  @PostMapping("/listIsComplete/{tId}")
  public List<TodoEntity> listIsComplete(@PathVariable("tId") Long travId, char todoComplete) {
    return todoService.listIsComplete(travId, todoComplete);
  }

////  To do list 완료 보기
//  @GetMapping("/listComplete/{tId}")
//  public List<TodoEntity> listComp(@PathVariable("tId") Long tId) {
//    return todoService.listComp(tId);
//  }

//  To do list 추가
  @PostMapping("/save/{tId}")
  public TodoEntity save(@PathVariable("tId") Long travId, @RequestBody TodoEntity todoEntity) {
    return todoService.save(travId, todoEntity);
  }

//  To do list 수정
  @PutMapping("/update/{tdId}")
  public TodoEntity update(@PathVariable("tdId") Long todoId, @RequestBody TodoEntity todoEntity) {
    return todoService.update(todoId, todoEntity);
  }

//  To do list 삭제
  @DeleteMapping("/delete/{tdId}")
  public void delete(@PathVariable("tdId") Long todoId) {
    todoService.deleteByTodoId(todoId);
  }
}
