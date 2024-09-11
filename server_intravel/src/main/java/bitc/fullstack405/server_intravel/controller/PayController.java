package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.PayEntity;
import bitc.fullstack405.server_intravel.repository.PayRepository;
import bitc.fullstack405.server_intravel.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("/moneyList/{mId}")
    public List<PayEntity> mIdlist(@PathVariable("mId") Long moneyId) {
        return payService.findMidAll(moneyId);
    }

    @GetMapping("/travList/{tId}")
    public List<PayEntity> tIdlist(@PathVariable("tId") Long travId) {
        return payService.findTidAll(travId);
    }

    @PostMapping("/save/{mId}")
    public PayEntity save(@PathVariable("mId") Long moneyId, @RequestBody PayEntity payEntity) {
        return payService.save(moneyId,payEntity);
    }

    @PutMapping("/update/{pId}")
    public PayEntity update(@PathVariable("pId") Long payId, @RequestBody PayEntity payEntity) {
        return payService.update(payId, payEntity);
    }

    @DeleteMapping("/delete/{pId}")
    void delete(@PathVariable("pId") Long payId) {
        payService.delete(payId);
    }
}
