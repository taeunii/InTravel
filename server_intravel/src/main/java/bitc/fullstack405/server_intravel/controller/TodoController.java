package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.TodoEntity;
import bitc.fullstack405.server_intravel.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todolist")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

//  To do list 전체보기
  @GetMapping("/listAll/{tId}")
  public List<TodoEntity> listAll(@PathVariable("tId") Long tId) {
    return todoService.listAll(tId);
  }

//  To do list 미완료 보기
  @GetMapping("/listIncomplete/{tId}")
  public List<TodoEntity> listUncomp(@PathVariable("tId") Long tId, @RequestBody char tdComplete) {
    return todoService.listUncomp(tId, tdComplete);
  }

////  To do list 완료 보기
//  @GetMapping("/listComplete/{tId}")
//  public List<TodoEntity> listComp(@PathVariable("tId") Long tId) {
//    return todoService.listComp(tId);
//  }

//  To do list 추가
  @PostMapping("/save/{tId}")
  public TodoEntity save(@PathVariable("tId") Long tId, @RequestBody TodoEntity todoEntity) {
    return todoService.save(tId, todoEntity);
  }

//  To do list 수정
  @PutMapping("update/{tdId}")
  public TodoEntity update(@PathVariable("tdId") Long tdId, @RequestBody TodoEntity todoEntity) {
    return todoService.update(tdId, todoEntity);
  }

//  To do list 삭제
  @DeleteMapping("delete/{tdId}")
  public void delete(@PathVariable("tdId") Long tdId) {
    todoService.deleteBytId(tdId);
  }
}
