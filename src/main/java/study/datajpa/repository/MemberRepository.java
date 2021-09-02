package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.UsernameOnlyDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.custom.MemberRepositoryCustom;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * Custom Repository
 *
 * <p>* 이것이 가능한 이유
 *
 * <pre>
 *     - 실제 JAVA 에서는 이렇게 동작하지 않음
 *     - Spring Data JPA 에서 해당 MemberRepositoryCustom 을 구현한 class 를 직접 생성하여, 사용할 수 있도록 만들어 주는 것
 * </pre>
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

  /**
   * 메소드에 Query 작성
   *
   * <p>! 실무에서 많이 쓰인다.
   *
   * <p>* 장점
   *
   * <pre>
   *     - 메소드 이름으로 인한 쿼리 생성 기능은 메소드 이름이 너무 길어지지만, 이것은 그것을 방지할 수 있다.
   *     - Application Loading 시점에, Query 를 파싱하여, 실행 시 문법 오류를 확인할 수 있다.
   * </pre>
   *
   * @param username
   * @param age
   * @return
   */
  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);

  /**
   * 단일 값으로 조회
   *
   * @return
   */
  @Query("select m.username from Member m")
  List<String> findUsernameList();

  /**
   * DTO 로 조회하기
   *
   * <pre>
   *     - select 절에 DTO 생성자를 넣어 사용한다.
   * </pre>
   *
   * @return
   */
  @Query(
      "select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  List<MemberDto> findUserDto();

  /**
   * 파라미터 바인딩 중 Collection 파라미터
   *
   * @param names
   * @return
   */
  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") List<String> names);

  /**
   * 반환 타입
   *
   * <pre>
   *     - List, 단건, Optional 등 유연하게 사용 가능
   *     - Collection 인 경우 값이 없을 경우 Empty Collection 이 반환됨 - 절대 null 이 아님
   *     - 단건 인 경우 값이 없으면 null, 두개 이상인 경우 IncorrectResultSizeDataAccessException(Spring Data) -> NonUniqueResultException 터짐 - 값이 없는 경우 순수 JPA 인 경우 NoResultException 이 터짐
   *     - Optional 인 경우, 두개 이상인 경우 IncorrectResultSizeDataAccessException(Spring Data) -> NonUniqueResultException 터짐 - 값이 없는 경우 Optional.Empty 임
   * </pre>
   *
   * @param username
   * @return
   */
  List<Member> findListByUsername(String username);

  Member findOneByUsername(String username);

  Optional<Member> findOptionalByUsername(String username);

  /**
   * Pagination
   *
   * <pre>
   *     - count query 를 실행하여 페이징 처리를 한다.
   * </pre>
   */
  Page<Member> findByAge(int age, Pageable pageable);

  /**
   * Slice
   *
   * <pre>
   *     - count query 를 실행하지 않는다.
   *     - limit 보다 + 1 만큼 더 가져와 다음 페이지 여부만 판단한다.
   *     - 다음 페이지를 가져올 때 nextPageable() 하여 가져오면 된다.
   * </pre>
   *
   * @param age
   * @param pageable
   * @return
   */
  Slice<Member> findSliceByAge(int age, Pageable pageable);

  /**
   * Count query 분리
   *
   * <p>! 실무에서 많이 쓰임
   *
   * <pre>
   *     - 복잡한 Query 라면 count query 를 같이 실행 할 경우 성능이 현저히 떨어질 수 있음
   *     - Spring Data JPA 에서 count query 를 분리할 수 있음
   *     - 단, @Query 를 사용해야함
   * </pre>
   *
   * @param age
   * @param pageable
   * @return
   */
  @Query(
      value = "select m from Member m left join m.team t where m.age >= :age",
      countQuery = "select count(m) from Member m")
  Page<Member> findExtractCountByAge(@Param("age") int age, Pageable pageable);

  /**
   * Bulk Modify Query
   *
   * <pre>
   *     - 한번에 update 하는 bulk 성 query 일때 사용한다.
   *     - 무조건, @Modifying 을 선언해주어야 한다. (안해주면 update 쿼리를 실행하지 않고 다른걸 실행해버림)
   * </pre>
   *
   * ! 주의해야할 점
   *
   * <pre>
   *     - bulk Modify Query 는 Persistence Context 무시하고 실행해버림
   *     - Persistence Context 에는 bulk 연산은 무시하기 때문에, bulk 연산 이전 데이터가 영속되어져 있음
   *     - bulk 연산을 사용 후 Persistence Context 를 사용하려면 다음과 같이 방법이 있다.
   *        1. bulk 연산 사용 후 Persistence Context 를 비우고, 다시 조회하여 사용 - flush(), clear() 후 사용
   *        2. 맨 처음 bulk 연산 후 Persistence Context 를 조회하여 사용
   *        3. @Modifying 의 clearAutomatically 옵션을 사용하면 자동으로 flush(), clear() 를 해준다.
   * </pre>
   *
   * @param age
   * @return
   */
  @Modifying(clearAutomatically = true)
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  /**
   * Fetch Join
   *
   * <pre>
   *     - Lazy 로딩이여도, Query 실행 할때 같이 가져옴
   * </pre>
   *
   * @return
   */
  @Query("select m from Member m left join fetch m.team t")
  List<Member> findMemberFetchJoin();

  /**
   * Entity Graph
   *
   * <pre>
   *     - Spring Data JPA 에서 Fetch Join 을 항상 JPQL 로 작성하기엔 너무 귀찮으므로 제공하준다.
   *     - @EntityGraph 를 선언한다.
   *        - attributePaths 에 fetch join 할 entity 를 적어주면 된다.
   *        - override, @Query, 메소드 이름으로 인한 쿼리 생성, JPA 에서 지원하는 Entity Graph 등 모두 사용 가능하다.
   *     - 기본이 left join 이 된다.
   * </pre>
   *
   * @return
   */
  @Override
  @EntityGraph(attributePaths = "team")
  List<Member> findAll();

  @EntityGraph(attributePaths = "team")
  @Query("select m from Member m")
  List<Member> findMemberEntityGraph();

  @EntityGraph(attributePaths = "team")
  List<Member> findEntityGraphByUsername(@Param("username") String username);

  @EntityGraph("Member.all")
  List<Member> findJPAEntityGraphByUsername(@Param("username") String username);

  /**
   * * JPA Hint - (SQL Hint 아님 주의)
   *
   * <pre>
   *     - JPA 내부 매커니즘으로 최적화 되어 있지만, 결론적으로 Dirty Checking 을 위해 entity 와 snapshot 를 사용하여 확인하기 때문에, 여기서 성능적 비용이 발생한다.
   *     - Hibernate 에 Hint 를 제공하여 그것을 방지 할 수 있다.
   *     - readOnly 를 하면 JPA 내부적으로 최적화 해버림
   * </pre>
   *
   * ! 주의할 점
   *
   * <pre>
   *     - hint 를 readOnly 를 true 로 하면 snapshot 을 생성하지 않으므로, dirty checking 을 하지 않음
   *     - 보통 hint 없이 성능이 잘 나오기 때문에, 처음부터 사용하지 말자 - 성능을 보고 나중에 결정해도 됨
   * </pre>
   */
  @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  /**
   * select update lock
   *
   * <pre>
   *     - JPA 에서 LOCK 지원
   *     - Spring Data JPA 에서 사용하기 편하게 만든것
   *     - dialect 를 기반으로 하지만, DB 매뉴얼을 보고 사용 권장
   *     - 단, 대규모 트래픽에서 가급적 사용하지 말자
   *        - 이때는, version 을 관리하는 lock 으로 해결하자
   * </pre>
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Member> findLockByUsername(String username);

  List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

  List<UsernameOnlyDto> findProjections2ByUsername(@Param("username") String username);

  /**
   * 동적 Projection
   *
   * @param username
   * @param type
   * @param <T>
   * @return
   */
  <T> List<T> findProjections3ByUsername(@Param("username") String username, Class<T> type);

  /**
   * native query
   *
   * <pre>
   *     - 실제 DB query  가 실행된다.
   *     - member 테이블의 모든 column 을 모두 반환해야 된다.
   *     - 반환타입 지원 범위가 좁다.
   *     - 문법 확인 불가 - 직접 메소드를 실행해야 알 수 있다.
   *     - 실무에서는 그냥 JdbcTemplate, Mybatis 를 사용하는게 좋다.
   * </pre>
   *
   * * 하지만, Projections 랑 같이 쓸수 있다.
   *
   * <pre>
   *     - pagination 까지 가능
   *     - 단, 컬럼 이름을 매칭시켜주어야 한다.
   * </pre>
   */
  @Query(value = "select * from member where username = ?", nativeQuery = true)
  Member findByNativeQuery(String username);

  @Query(
      value =
          "select m.member_id as id, m.username, t.name as teamName "
              + "from member m left join team t",
      countQuery = "select count(*) from member",
      nativeQuery = true)
  Page<MemberProjection> findProjectionsByNativeQuery(Pageable pageable);
}
