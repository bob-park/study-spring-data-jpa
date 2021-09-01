package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

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
}
