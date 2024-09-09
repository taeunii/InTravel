package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.MoneyEntity;
import bitc.fullstack405.server_intravel.repository.MoneyRepository;
import bitc.fullstack405.server_intravel.repository.PayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;
    private final PayRepository payRepository;

    public List<MoneyEntity> findAll(Long travId) {
        return moneyRepository.findByTravId(travId);
    }

    public MoneyEntity save(Long travId, MoneyEntity moneyEntity) {

        moneyEntity.setTravId(travId);

        return moneyRepository.save(moneyEntity);
    }

    @Transactional
    public MoneyEntity update(Long moneyId, MoneyEntity moneyEntity) {
        MoneyEntity updateMoney = moneyRepository.findById(moneyId).get();

        updateMoney.setMoneyTitle(moneyEntity.getMoneyTitle());
        updateMoney.setExpenses(moneyEntity.getExpenses());

        return moneyRepository.save(updateMoney);
    }

    @Transactional
    public void delete(Long moneyId) {
        payRepository.deleteByMoneyId(moneyId);
        moneyRepository.deleteById(moneyId);
    }
}
