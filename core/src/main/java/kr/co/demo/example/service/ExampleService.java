package kr.co.demo.example.service;

import kr.co.demo.attach.domain.AttachBag;
import kr.co.demo.attach.domain.ReferenceTypeRegistry;
import kr.co.demo.attach.service.AttachService;
import kr.co.demo.example.domain.Example;
import kr.co.demo.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExampleService {

  @Autowired
  private ExampleRepository exampleRepository;

  @Autowired
  private AttachService attachService;

  /**
   * 예제 게시판 카운트 조회
   *
   * @return 조건에 해당하는 예제 게시판 카운트
   */
  @Transactional(readOnly = true)
  public int getExampleCount(Example example) {
    return exampleRepository.selectExampleCount(example);
  }

  /**
   * 예제 게시판 리스트 조회
   *
   * @return 조건에 해당하는 예제 게시판 리스트
   */
  @Transactional(readOnly = true)
  public List<Example> getExampleList(Example example) {
    return exampleRepository.selectExampleList(example);
  }

  /**
   * 예제 게시판 조회
   *
   * @return 조건에 해당하는 예제 게시판
   */
  @Transactional(readOnly = true)
  public Example getExample(Long idx) {
    return exampleRepository.selectExample(idx);
  }

  /**
   * 예제 게시판 등록
   * @param example
   * @return int
   */
  @Transactional
  public boolean insertExample(Example example) throws Exception {
    boolean isSuccess = exampleRepository.insertExample(example) > 0;
    if(isSuccess) {
      AttachBag attachBag = example.getAttachBag();
      attachService.save(ReferenceTypeRegistry.EXAMPLE, String.valueOf(example.getIdx()), attachBag);
    }
    return isSuccess;
  }

  /**
   * 예제 게시판 수정
   * @param example
   * @return int
   */
  @Transactional
  public boolean updateExample(Example example) throws Exception {
    boolean isSuccess = exampleRepository.updateExample(example) > 0;
    if(isSuccess) {
      AttachBag attachBag = example.getAttachBag();
      attachService.save(ReferenceTypeRegistry.EXAMPLE, String.valueOf(example.getIdx()), attachBag);
    }
    return isSuccess;
  }

  /**
   * 예제 게시판 삭제
   * @param example
   * @return
   */
  @Transactional
  public int deleteExample(Example example) throws Exception {
    return exampleRepository.deleteExample(example);
  }

}
