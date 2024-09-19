package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.MapEntity;
import bitc.fullstack405.server_intravel.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/list/{tId}")
    public List<MapEntity> mapList(@PathVariable("tId") Long travId) {
        return mapService.findAll(travId);
    }
//    travId기준 맵 1개만 출력이 필요할경우 이거 사용
    @GetMapping("/mapEach/{tId}/{mId}")
    public MapEntity Map(@PathVariable("tId") Long travId, @PathVariable("mId") Long mapId) {
        return mapService.findBytIdAndmId(travId, mapId);
    }

    @PostMapping("/save/{tId}")
    public MapEntity save(@PathVariable("tId") Long travId, @RequestBody MapEntity mapEntity) {
        return mapService.save(travId, mapEntity);
    }

    @PutMapping("/update/{mId}")
    public MapEntity update(@PathVariable("mId") Long mapId, @RequestBody MapEntity mapEntity) {
        return mapService.update(mapId, mapEntity);
    }

    @DeleteMapping("/delete/{mId}")
    public void delete(@PathVariable("mId") Long mapId) {
        mapService.delete(mapId);
    }
}
