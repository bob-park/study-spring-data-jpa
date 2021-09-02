package study.datajpa.entity;

import javax.persistence.*;

@Entity
@NamedQuery(
    name = "Member.findByUsername",
    query = "select m from Member m where m.username = :username")

// JPA Entity Graph
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
/**
 * Auditing - 순수 JPA
 *
 * <pre>
 *     - JPA persist 특정 이벤트 발생시 실행하고 싶을 떄, 사용
 *     - 공통 기능을 더이상 개발자가 신경안써도 되니 아주 좋다.
 * </pre>
 */
//public class Member extends JpaBaseEntity{
public class Member extends BaseEntity{

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String username;
  private int age;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  protected Member() {}

  public Member(String username) {
    this.username = username;
  }

  public Member(String username, int age, Team team) {
    this.username = username;
    this.age = age;

    if (team != null) {
      changeTeam(team);
    }
  }

  public Member(String username, int age) {
    this.username = username;
    this.age = age;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  @Override
  public String toString() {
    return "Member{" + "id=" + id + ", username='" + username + '\'' + ", age=" + age + '}';
  }
}
