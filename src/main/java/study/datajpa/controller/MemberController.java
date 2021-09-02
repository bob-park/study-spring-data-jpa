package study.datajpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
public class MemberController {

  private final MemberRepository memberRepository;

  public MemberController(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @PostConstruct
  public void init() {
    memberRepository.save(new Member("member1"));
  }

  @GetMapping(path = "/members/{id}")
  public String findMember(@PathVariable Long id) {
    Member findMember = memberRepository.findById(id).get();

    return findMember.getUsername();
  }

  /**
   * Domain Class Converter
   *
   * <pre>
   *     - Spring Data JPA 에서 @PathVariable 로 받은 ID 를 가지고 알아서 Member 를 주입해준다.
   *     - 근데 권장하지는 않음
   * </pre>
   *
   * ! 주의사항
   *
   * <pre>
   *     - 반드시 @PathVariable 의 name 을 적어주어야 한다.
   *     - 반드시 조회용으로만 사용해야함 - Transaction 없이 사용한 거라, Dirty Checking 이 이루어지지 않는다.
   * </pre>
   *
   * @param member
   * @return
   */
  @GetMapping(path = "/members2/{id}")
  public String findMember(@PathVariable("id") Member member) {
    return member.getUsername();
  }
}
