package bitc.fullstack405.server_intravel.repository;

import bitc.fullstack405.server_intravel.entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Long> {

  @Query("select m from MemoEntity m where m.travId = :travId order by m.choiceDate")
  List<MemoEntity> findByTravId(Long travId);

  MemoEntity findByMemoId(Long memoId);

  void deleteByMemoId(Long memoId);

  void deleteByTravId(Long travelId);
}
