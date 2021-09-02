package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

  /**
   * Open Projection
   *
   * <pre>
   *     - DB Query 실행 시 entity 의 모든 필드를 가져와서 계산하는 것
   * </pre>
   *
   * ! Closed Projection
   *
   * <pre>
   *     - entity 의 정확한 필드를 가져와 리턴해주는 것
   * </pre>
   *
   * @return
   */
  @Value("#{target.username + ' ' + target.age}")
  String getUsername();
}
