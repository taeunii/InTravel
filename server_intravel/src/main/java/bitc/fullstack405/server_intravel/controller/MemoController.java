package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.MemoEntity;
import bitc.fullstack405.server_intravel.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

  private final MemoService memoService;

  @GetMapping("/list/{tId}")
  public List<MemoEntity> listAll(@PathVariable("tId") Long tId) {
    return memoService.listAll(tId);
  }

  @PostMapping("/save/{tId}")
  public MemoEntity save(@PathVariable("tId") Long tId, @RequestBody MemoEntity memoEntity) {
    return memoService.save(tId, memoEntity);
  }

  @PutMapping("/update/{mId}")
  public MemoEntity update(@PathVariable("mId") Long mId, @RequestBody MemoEntity memoEntity) {
    return memoService.update(mId, memoEntity);
  }

  @DeleteMapping("/delete/{mId}")
  public void delete(@PathVariable("mId") Long mId) {
    memoService.deleteByMId(mId);
  }

}
