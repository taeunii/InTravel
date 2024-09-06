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

    MemoEntity updateMemo = memoRepository.findByMemoId(memoId);

    updateMemo.setMemoContent(memoEntity.getMemoContent());
    updateMemo.setMemoTitle(memoEntity.getMemoTitle());

    return updateMemo;
  }

  public void deleteByMemoId(Long memoId) {
    memoRepository.deleteById(memoId);
  }
}
