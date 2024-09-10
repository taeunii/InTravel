package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
  List<PhotoEntity> findByTravId(Long travId);
}
