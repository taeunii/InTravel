package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.MapEntity;
import bitc.fullstack405.server_intravel.repository.MapRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    public List<MapEntity> findAll(Long travId) {
        return mapRepository.findByTravId(travId);
    }

    public MapEntity findBytIdAndmId(Long travId, Long mapId) {
        MapEntity mapEntity = mapRepository.findByTravIdAndMapId(travId, mapId);

        if (mapEntity == null) {
            return null;
        }
        return mapEntity;
    }

    public MapEntity save(Long travId, MapEntity mapEntity) {

        mapEntity.setTravId(travId);

        return mapRepository.save(mapEntity);
    }

    @Transactional
    public MapEntity update(Long mapId, MapEntity mapEntity) {
        MapEntity updateMap = mapRepository.findById(mapId).get();

        updateMap.setLatitude(mapEntity.getLatitude());
        updateMap.setLongitude(mapEntity.getLongitude());
        updateMap.setPinName(mapEntity.getPinName());
        updateMap.setPinColor(mapEntity.getPinColor());

        return mapRepository.save(updateMap);
    }

    public void delete(Long mapId) {
        mapRepository.deleteById(mapId);
    }
}
