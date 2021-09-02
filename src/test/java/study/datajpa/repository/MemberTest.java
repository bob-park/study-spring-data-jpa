package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

  @Autowired private EntityManager em;
  @Autowired private MemberRepository memberRepository;

  @Test
  void testJpaEventBaseEntity() throws Exception {
    // given
    Member member1 = new Member("member1");

    memberRepository.save(member1); // @PrePersist

    Thread.sleep(1000);
    member1.setUsername("member2");

    em.flush(); // @PreUpdate
    em.clear();

    // when
    Member findMember = memberRepository.findById(member1.getId()).get();

    // then
    System.out.println("findMember = " + findMember);
    //    System.out.println("createdDate = " + findMember.getCreatedDate());
    //    System.out.println("updatedDate = " + findMember.getUpdatedDate());
    System.out.println("createdDate = " + findMember.getCreateAt());
    System.out.println("updatedDate = " + findMember.getUpdateAt());
    System.out.println("createdBy = " + findMember.getCreatedBy());
    System.out.println("updatedBy = " + findMember.getLastModifiedBy());
  }
}
