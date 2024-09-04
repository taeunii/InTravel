package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.TravelEntity;
import bitc.fullstack405.server_intravel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    public List<TravelEntity> findAll() {

//        LocalDate nowDate = LocalDate.now();

        List<TravelEntity> travels = travelRepository.findAll();

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (TravelEntity travel : travels) {
//            int startDate = Integer.parseInt(travel.getStartDate());
//            int endDate = Integer.parseInt(travel.getEndDate());
//            long daysStart = DAYS.between(nowDate, startDate);
//            long daysStart = Integer.parseInt(nowDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))) - startDate;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate date1 = LocalDate.parse(travel.getStartDate(), formatter);
            LocalDate date2 = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            Long days = DAYS.between(date1, date2);

            travel.setDDay(days);

        }

        travelRepository.saveAll(travels);

        return travels;
    }

    public TravelEntity save(TravelEntity travelEntity) {
        return travelRepository.save(travelEntity);
    }

    public TravelEntity updateTravel(Long id, TravelEntity travelEntity) {
        TravelEntity travel = travelRepository.findById(id).get();

        travel.setTTitle(travelEntity.getTTitle());
        travel.setStartDate(travelEntity.getStartDate());
        travel.setEndDate(travelEntity.getEndDate());
        travel.setCate(travelEntity.getCate());

        return travelRepository.save(travel);
    }
}
