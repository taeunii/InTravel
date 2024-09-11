package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.MemoEntity;
import bitc.fullstack405.server_intravel.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

  private final MemoService memoService;

  @GetMapping("/listAll/{tId}")
  public List<MemoEntity> listAll(@PathVariable("tId") Long travId) {
    return memoService.listAll(travId);
  }

  @PostMapping("/save/{tId}")
  public MemoEntity save(@PathVariable("tId") Long travId, @RequestBody MemoEntity memoEntity) {
    memoEntity.setMemoCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

    return memoService.save(travId, memoEntity);
  }

  @PutMapping("/update/{mId}")
  public MemoEntity update(@PathVariable("mId") Long memoId, @RequestBody MemoEntity memoEntity) {
    return memoService.update(memoId, memoEntity);
  }

  @DeleteMapping("/delete/{mId}")
  public void delete(@PathVariable("mId") Long memoId) {
    memoService.deleteByMemoId(memoId);
  }

}
