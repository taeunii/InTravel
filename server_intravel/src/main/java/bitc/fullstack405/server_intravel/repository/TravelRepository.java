package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
