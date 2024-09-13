package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.MoneyEntity;
import bitc.fullstack405.server_intravel.service.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/money")
@RequiredArgsConstructor
public class MoneyController {

  private final MoneyService moneyService;

  @GetMapping("/list/{tId}")
  public List<MoneyEntity> MoneyList(@PathVariable("tId") Long travId) {
    return moneyService.findAll(travId);
  }

  @PostMapping("/save/{tId}")
  public MoneyEntity save(@PathVariable("tId") Long travId, @RequestBody MoneyEntity moneyEntity) {
    return moneyService.save(travId, moneyEntity);
  }

  @PutMapping("/update/{mId}")
  public MoneyEntity update(@PathVariable("mId") Long moneyId, @RequestBody MoneyEntity moneyEntity) {
    return moneyService.update(moneyId, moneyEntity);
  }

  @DeleteMapping("/delete/{mId}")
  void delete(@PathVariable("mId") Long moneyId) {
    moneyService.delete(moneyId);
  }
}
