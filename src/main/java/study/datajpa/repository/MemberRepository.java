package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  /**
   * Named Query 이용
   *
   * <p>* 주의할 점
   *
   * <pre>
   *     - 메소드 이름은 아무거나 상관없다.
   *     - @Query 는 관례가 있다.
   *        - @Query 가 없는 경우 entity 와 메소드 명을 가지고 named query 를 찾는다.
   *        - named query 가 없는 경우 메소드 이름으로 쿼리를 생성한다.
   *     - Named Query 일 때, 반드시, @Param 을 명시해주어야 한다.
   *     - Named Query 는 Application Loading 시점에, 파싱을 하기 때문에, 문법 오류를 바로 알 수 있다. - 장점
   * </pre>
   *
   * ! 실무에서 거의 사용하지 않는다.
   *
   * <pre>
   *     - entity 에 Query 가 들어가기 때문에 지저분하다.
   *     - Spring Data JPA 에서 메소드에 Query 를 직접 사용할 수 있는 기능을 제공하기 때문에 필요없다.
   * </pre>
   *
   * @param username
   * @return
   */
  @Query(name = "Member.findByUsername")
  List<Member> findByUsername(@Param("username") String username);
}