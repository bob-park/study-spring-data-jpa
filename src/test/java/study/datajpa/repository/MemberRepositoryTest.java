package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

  @Autowired private MemberRepository memberRepository;

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
  void findByUsernameAndAgeGreaterThan() throws Exception{
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
  void testNamedQuery() throws Exception{
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
}