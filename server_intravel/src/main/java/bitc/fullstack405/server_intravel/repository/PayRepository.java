package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.PayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayRepository extends JpaRepository<PayEntity, Long> {

    List<PayEntity> findByMoneyId(Long moneyId);

    List<PayEntity> findByTravId(Long travId);

    void deleteByMoneyId(Long moneyId);

}
