package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

  @Autowired private EntityManager em;

  @Autowired private MemberRepository memberRepository;
  @Autowired private TeamRepository teamRepository;

  @Test
  void testmember() throws Exception {
    // given
    Member member = new Member("memberB");
    Member savedMember = memberRepository.save(member);

    // when
    Member findMember = memberRepository.findById(savedMember.getId()).get();

    // then
    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member);
  }

  @Test
  void basicCRUD() throws Exception {
    // given
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when

    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();

    // then

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    findMember1.setUsername("member1!!!");

    List<Member> all = memberRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    long count = memberRepository.count();
    assertThat(count).isEqualTo(2);

    memberRepository.delete(member1);
    memberRepository.delete(member2);

    long deletedCount = memberRepository.count();
    assertThat(deletedCount).isZero();
  }

  @Test
  void findByUsernameAndAgeGreaterThan() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("user2", 15);

    // then

    assertThat(result.get(0).getUsername()).isEqualTo("user2");
    assertThat(result.get(0).getAge()).isEqualTo(20);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void testNamedQuery() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    List<Member> result = memberRepository.findByUsername("user2");

    // then
    assertThat(result.get(0).getUsername()).isEqualTo("user2");
    assertThat(result.get(0).getAge()).isEqualTo(20);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void testQuery() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    List<Member> result = memberRepository.findUser("user1", 10);

    // then
    assertThat(result.get(0).getUsername()).isEqualTo("user1");
    assertThat(result.get(0).getAge()).isEqualTo(10);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void findUserNameList() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    List<String> result = memberRepository.findUsernameList();

    // then
    assertThat(result.get(0)).isEqualTo("user1");
  }

  @Test
  void findUserDto() throws Exception {
    // given
    Team teamA = new Team("teamA");

    teamRepository.save(teamA);

    Member member1 = new Member("user1", 10);

    member1.changeTeam(teamA);

    memberRepository.save(member1);

    // when
    List<MemberDto> result = memberRepository.findUserDto();

    // then
    assertThat(result.get(0).getUsername()).isEqualTo("user1");
    assertThat(result.get(0).getTeamName()).isEqualTo("teamA");
  }

  @Test
  void findByNames() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    List<Member> result = memberRepository.findByNames(Arrays.asList("user1", "user2"));

    // then
    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  void testReturnType() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    memberRepository.findListByUsername("user1"); // Collection
    memberRepository.findOneByUsername("user1"); // 단일
    memberRepository.findOptionalByUsername("user3"); // Optional

    // then
  }

  @Test
  void testPagination() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 10);
    Member member3 = new Member("user3", 10);
    Member member4 = new Member("user4", 10);
    Member member5 = new Member("user5", 10);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
    memberRepository.save(member5);

    // when

    int age = 10;

    // ! Spring data jpa 는 page 가 0부터 시작
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    Page<Member> result = memberRepository.findByAge(age, pageRequest);

    long totalCount = result.getTotalElements();
    List<Member> content = result.getContent();

    // then
    assertThat(totalCount).isEqualTo(5);
    assertThat(content.size()).isEqualTo(3);
    assertThat(result.getTotalPages()).isEqualTo(2);
    assertThat(result.getNumber()).isZero();
    assertThat(result.isFirst()).isTrue();
    assertThat(result.hasNext()).isTrue();
  }

  @Test
  void testSlicePagination() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 10);
    Member member3 = new Member("user3", 10);
    Member member4 = new Member("user4", 10);
    Member member5 = new Member("user5", 10);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
    memberRepository.save(member5);

    // when

    int age = 10;

    // ! Spring data jpa 는 page 가 0부터 시작
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    Slice<Member> result = memberRepository.findSliceByAge(age, pageRequest);

    List<Member> content = result.getContent();

    // then
    assertThat(content.size()).isEqualTo(3);
    assertThat(result.getNumber()).isZero();
    assertThat(result.isFirst()).isTrue();
    assertThat(result.hasNext()).isTrue();

    Slice<Member> nextResult = memberRepository.findSliceByAge(age, result.nextPageable());

    assertThat(nextResult.getContent().size()).isEqualTo(2);
    assertThat(nextResult.getNumber()).isEqualTo(1);
    assertThat(nextResult.isFirst()).isFalse();
    assertThat(nextResult.hasNext()).isFalse();
  }

  @Test
  void testPaginationExtractCount() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 10);
    Member member3 = new Member("user3", 10);
    Member member4 = new Member("user4", 10);
    Member member5 = new Member("user5", 10);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
    memberRepository.save(member5);

    // when

    int age = 10;

    // ! Spring data jpa 는 page 가 0부터 시작
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    Page<Member> result = memberRepository.findExtractCountByAge(age, pageRequest);

    List<Member> content = result.getContent();

    // then
    assertThat(result.getTotalElements()).isEqualTo(5);
    assertThat(content.size()).isEqualTo(3);
    assertThat(result.getNumber()).isZero();
    assertThat(result.isFirst()).isTrue();
    assertThat(result.hasNext()).isTrue();

    // * Page to DTO
    // page 를 유지하면서 DTO 로 변환 된다.
    Page<MemberDto> toMap =
        result.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
  }

  @Test
  void testBulkUpdate() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 19);
    Member member3 = new Member("user3", 20);
    Member member4 = new Member("user4", 21);
    Member member5 = new Member("user5", 40);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
    memberRepository.save(member5);

    // when
    int result = memberRepository.bulkAgePlus(20);

    // then
    assertThat(result).isEqualTo(3);
  }

  @Test
  void findMemberLazy() throws Exception {
    // given
    // member1 -> teamA
    // member2 -> teamB
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");

    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 10, teamB);

    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    // when
    //    List<Member> members = memberRepository.findAll();
    //    List<Member> members = memberRepository.findMemberFetchJoin();
    //    List<Member> members = memberRepository.findAll();
    //    List<Member> members = memberRepository.findMemberEntityGraph();
    //    List<Member> members = memberRepository.findEntityGraphByUsername("member1");
    List<Member> members = memberRepository.findJPAEntityGraphByUsername("member1");

    for (Member member : members) {
      System.out.println("member = " + member);
      System.out.println("member.teamName = " + member.getTeam().getName());
    }

    // then
  }

  @Test
  void testQueryHint() throws Exception {
    // given
    Team teamA = new Team("teamA");
    teamRepository.save(teamA);

    Member member1 = new Member("member1", 10, teamA);
    memberRepository.save(member1);

    em.flush();
    em.clear();

    // when
    //    Member findMember = memberRepository.findById(member1.getId()).get();
    Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());

    findMember.setUsername("member2");

    // 내부 메커니즘에 의해 변경되지만, 확인하려면 객체를 2개 가지고 있어야한다. 즉, 성능적 비용이 발생한다.
    // 이러한 부분을 위해 Hibernate 에서 Hint 제공
    em.flush(); // Dirty Checking

    // then
  }

  @Test
  void testLock() throws Exception {
    // given
    Team teamA = new Team("teamA");
    teamRepository.save(teamA);

    Member member1 = new Member("member1", 10, teamA);
    memberRepository.save(member1);

    em.flush();
    em.clear();
    // when
    List<Member> result = memberRepository.findLockByUsername("member1");

    for (Member member : result) {
      member.setUsername("member2");
    }

    // then
  }

  @Test
  void testCallCustom() throws Exception {
    // given

    // when
    List<Member> memberCustom = memberRepository.findMemberCustom();

    // then
  }

  /**
   * Query by Example
   *
   * <p>* entity 로 바로 검색 조건을 만드는 것
   *
   * <pre>
   *     - 실제 entity 객체를 검색조건으로 만들어 사용하는 것
   *     - JpaRepository 에 기본으로 구현되어 있음
   *     - 하지만, 실무에서는 거의 사용하지 못한다.
   *        - Inner Join 만 가능하다.
   *        - Outer Join 이 안된다.
   *        - 복잡한 join 이 안된다.
   *        - 중첩 적인 제약조건이 안된다.
   *        - 매칭 조건이 매우 단순해서 못쓴다. (문자를 제외하면, equals 만 지원
   * </pre>
   *
   * @throws Exception
   */
  @Test
  void queryByExample() throws Exception {
    // given
    Team teamA = new Team("teamA");

    em.persist(teamA);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 10, teamA);

    member1.changeTeam(teamA);
    member2.changeTeam(teamA);

    em.persist(member1);
    em.persist(member2);

    em.flush();
    em.clear();

    // when
    // Probe
    Member member = new Member("member1"); // 조건이 entity 가 된다. - 미친거 아니냐?
    Team team = new Team("teamA");

    member.setTeam(team); // 연관관계도 해준다.

    ExampleMatcher m = ExampleMatcher.matching().withIgnorePaths("age");

    Example<Member> example = Example.of(member, m);

    List<Member> result = memberRepository.findAll(example);

    // then
    assertThat(result.get(0).getUsername()).isEqualTo("member1");
  }
}
