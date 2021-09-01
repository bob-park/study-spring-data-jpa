package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

  /**
   * 메소드 이름으로 JPQL 를 생성할 수 있다.
   *
   * <p>* 주의 할 점
   *
   * <pre>
   *     - 반드시 Rule 를 따라야한다.
   *     - 2개 이상의 조건이 있을 경우 다른 방법으로 해야한다. (메소드 이름이 너무 길어짐)
   * </pre>
   *
   * @param username
   * @param age
   * @return
   */
  List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
