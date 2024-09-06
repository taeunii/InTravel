package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.TravelEntity;
import bitc.fullstack405.server_intravel.repository.MemoRepository;
import bitc.fullstack405.server_intravel.repository.TodoRepository;
import bitc.fullstack405.server_intravel.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final TodoRepository todoRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public List<TravelEntity> findAll() {

        List<TravelEntity> travels = travelRepository.findAll();

        //******************************************************************** 삭제금지
        
//        날짜 계산 로직 짬
//        LocalDate nowDate = LocalDate.now();
//
//        for (TravelEntity travel : travels) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate startDate = LocalDate.parse(travel.getStartDate(), formatter);
//
//            Long days = ChronoUnit.DAYS.between(startDate, nowDate);
//            travel.setDDay(days);
//        }
////
//        travelRepository.saveAll(travels);
//
//        public String formatDDay(Long days) {
//        if (days > 0) {
//            return "+" + days.toString();
//        } else {
//            return days.toString();
//        }
//    }
//
//        ************************************************************************* 삭제금지
        return travels;
    }

    public TravelEntity save(TravelEntity travelEntity) {
        return travelRepository.save(travelEntity);
    }

    @Transactional
    public TravelEntity updateTravel(Long tId, TravelEntity travelEntity) {
        TravelEntity travel = travelRepository.findById(tId).get();

        travel.setTravTitle(travelEntity.getTravTitle());
        travel.setStartDate(travelEntity.getStartDate());
        travel.setEndDate(travelEntity.getEndDate());
        travel.setCate(travelEntity.getCate());

        return travelRepository.save(travel);
    }

    public List<TravelEntity> findByTravelComplete(char comp) {
        return travelRepository.findBytComplete(comp);
    }

    @Transactional
    public void delete(Long travId) {
        todoRepository.deleteByTravId(travId);
        memoRepository.deleteByTravId(travId);
        travelRepository.deleteById(travId);
    }
}
