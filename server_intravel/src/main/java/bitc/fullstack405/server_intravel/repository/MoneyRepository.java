package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.MoneyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRepository extends JpaRepository<MoneyEntity, Long> {

    List<MoneyEntity> findByTravId(Long travId);

    void deleteByTravId(Long travId);
}
