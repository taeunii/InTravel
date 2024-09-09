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

    @GetMapping("/list/{mId}")
    public List<PayEntity> list(@PathVariable("mId") Long moneyId) {
        return payService.findAll(moneyId);
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
