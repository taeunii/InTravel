package bitc.fullstack405.server_intravel.controller;

import bitc.fullstack405.server_intravel.entity.TravelEntity;
import bitc.fullstack405.server_intravel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @GetMapping("/list")
    public List<TravelEntity> TravelList() {
        return travelService.findAll();
    }

    @PostMapping("/insert")
    public TravelEntity insert(@RequestBody TravelEntity travelEntity) {
        return travelService.save(travelEntity);
    }

    @PutMapping("/update/{id}")
    public TravelEntity updateTravel(@PathVariable Long id, @RequestBody TravelEntity travelEntity) {
        return travelService.updateTravel(id, travelEntity);
    }

//    @DeleteMapping("/delete/{id}")
//    public void delete(@PathVariable Long id) {
//        travelService.delete(id);
//    }
}

