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

  @Test
  void findByUsernameAndAgeGreaterThen() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 20);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    // when
    List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("user2", 15);

    // then

    assertThat(result.get(0).getUsername()).isEqualTo("user2");
    assertThat(result.get(0).getAge()).isEqualTo(20);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void findByUsername() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    memberJpaRepository.save(member1);

    // when
    List<Member> result = memberJpaRepository.findByUsername("user1");

    // then
    assertThat(result.get(0).getUsername()).isEqualTo("user1");
    assertThat(result.get(0).getAge()).isEqualTo(10);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void testPagination() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 10);
    Member member3 = new Member("user3", 10);
    Member member4 = new Member("user4", 10);
    Member member5 = new Member("user5", 10);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);
    memberJpaRepository.save(member3);
    memberJpaRepository.save(member4);
    memberJpaRepository.save(member5);

    // when

    int age = 10;
    int offset = 0;
    int limit = 3;

    long totalCount = memberJpaRepository.totalCount(age);
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);

    // then
    assertThat(totalCount).isEqualTo(5);
    assertThat(members.size()).isEqualTo(3);
  }

  @Test
  void testSlicePage() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 10);
    Member member3 = new Member("user3", 10);
    Member member4 = new Member("user4", 10);
    Member member5 = new Member("user5", 10);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);
    memberJpaRepository.save(member3);
    memberJpaRepository.save(member4);
    memberJpaRepository.save(member5);

    // when

    int age = 10;
    int offset = 0;
    int limit = 3;

    long totalCount = memberJpaRepository.totalCount(age);
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);

    // then
    assertThat(totalCount).isEqualTo(5);
    assertThat(members.size()).isEqualTo(3);
  }

  @Test
  void testBulkUpdate() throws Exception {
    // given
    Member member1 = new Member("user1", 10);
    Member member2 = new Member("user2", 19);
    Member member3 = new Member("user3", 20);
    Member member4 = new Member("user4", 21);
    Member member5 = new Member("user5", 40);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);
    memberJpaRepository.save(member3);
    memberJpaRepository.save(member4);
    memberJpaRepository.save(member5);

    // when
    int result = memberJpaRepository.bulkAgePlus(20);

    // then
    assertThat(result).isEqualTo(3);
  }
}
