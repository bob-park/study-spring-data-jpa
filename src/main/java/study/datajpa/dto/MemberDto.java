package study.datajpa.dto;

import study.datajpa.entity.Member;

public class MemberDto {

  private final Long id;
  private final String username;
  private final String teamName;

  public MemberDto(Member member) {
    this(member.getId(), member.getUsername(), null);
  }

  public MemberDto(Long id, String username, String teamName) {
    this.id = id;
    this.username = username;
    this.teamName = teamName;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getTeamName() {
    return teamName;
  }
}
