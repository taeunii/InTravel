package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.MapEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<MapEntity,Long> {

    List<MapEntity> findByTravId(Long travId);

    void deleteByTravId(Long travId);

    MapEntity findByTravIdAndMapId(Long travId, Long mapId);
}
