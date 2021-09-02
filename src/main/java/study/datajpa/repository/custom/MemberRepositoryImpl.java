package study.datajpa.repository.custom;

import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * * 사용하는 이유
 *
 * <pre>
 *     - 복잡한 쿼리를 사용할 때
 *     - 직접 DB Query 를 실행하고 싶을 때
 *     - 실무에서는 QueryDSL 를 사용할 때 많이 씀
 * </pre>
 *
 * ! 지켜야할 규칙
 *
 * <pre>
 *     - Custom Repository 를 구현한 Class 의 이름이 중요
 *     - ex:) MemberRepositoryCustom 를 사용할 JPA Repository 가 MemberRepository 일 경우 Custom Repository 의 이름은 MemberRepository + Impl 이어야 한다.
 *     - 하지만, 뒤에 붙는 이름을 변경하는 방법이 있다. (하지만, 그냥 관례를 따르는 것이 좋음 - 유지보수 시 힘듬)
 *        - @EnableJpaRepositories(basePackages="...", repositoryImplementationPostfix="...") 하면 된다.
 * </pre>
 *
 * ! 사용시 참고
 *
 * <pre>
 *     - 간단하거나, JPA Repository 로만 사용할 수 있는 경우 거기서 끝내야한다.
 *     - 화면 UI 를 위한 query 와 핵심 비지니스 로직이 있는 경우 서로 분리하는 것이 좋음
 *     - 항상 Custom Repository 를 만들 필요는 없으므로, 별도의 repository 를 만들어 사용해도 괜찮음 - 설계 시 구조를 고민하여 사용해야 한다.
 * </pre>
 */
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final EntityManager em;

  public MemberRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public List<Member> findMemberCustom() {

    return em.createQuery("select m from Member m", Member.class).getResultList();
  }
}
