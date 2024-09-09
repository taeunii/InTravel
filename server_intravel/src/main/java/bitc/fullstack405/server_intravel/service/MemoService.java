package bitc.fullstack405.server_intravel.service;

import bitc.fullstack405.server_intravel.entity.MemoEntity;
import bitc.fullstack405.server_intravel.repository.MemoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

  private final MemoRepository memoRepository;


  public List<MemoEntity> listAll(Long travId) {
    return memoRepository.findByTravId(travId);
  }

  public MemoEntity save(Long travId, MemoEntity memoEntity) {
    memoEntity.setTravId(travId);
    return memoRepository.save(memoEntity);
  }

  @Transactional
  public MemoEntity update(Long memoId, MemoEntity memoEntity) {
    MemoEntity updateMemo = memoRepository.findById(memoId).get();

    updateMemo.setTravId(memoEntity.getTravId());
    updateMemo.setMemoTitle(memoEntity.getMemoTitle());
    updateMemo.setMemoContent(memoEntity.getMemoContent());
    updateMemo.setChoiceDate(memoEntity.getChoiceDate());

    return updateMemo;
  }

  @Transactional
  public void deleteByMemoId(Long memoId) {
    memoRepository.deleteByMemoId(memoId);
  }
}
