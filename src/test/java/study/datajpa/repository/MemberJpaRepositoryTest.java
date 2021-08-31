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
class MemberJpaRepositoryTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  void testMember() {
    // given
    Member member = new Member("memberA");
    Member savedMember = memberJpaRepository.save(member);

    // when
    Member findMember = memberJpaRepository.find(savedMember.getId());

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

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    // when

    Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
    Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

    // then

    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    findMember1.setUsername("member1!!!");

    List<Member> all = memberJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    long count = memberJpaRepository.count();
    assertThat(count).isEqualTo(2);

    memberJpaRepository.delete(member1);
    memberJpaRepository.delete(member2);

    long deletedCount = memberJpaRepository.count();
    assertThat(deletedCount).isZero();
  }
}
