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


  public List<MemoEntity> listAll(Long tId) {
    return memoRepository.findBytId(tId);
  }

  public MemoEntity save(Long tId, MemoEntity memoEntity) {
    memoEntity.setTId(tId);
    return memoRepository.save(memoEntity);
  }

  @Transactional
  public MemoEntity update(Long mId, MemoEntity memoEntity) {

    MemoEntity updateMemo = memoRepository.findBymId(mId);

    updateMemo.setMContent(memoEntity.getMContent());
    updateMemo.setMTitle(memoEntity.getMTitle());

    return updateMemo;
  }

  public void deleteByMId(Long mId) {
    memoRepository.deleteBymId(mId);
  }
}
