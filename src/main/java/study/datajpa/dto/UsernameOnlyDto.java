package study.datajpa.dto;

/**
 * Class 기반 Projection
 *
 */
public class UsernameOnlyDto {

  private final String username;

  public UsernameOnlyDto(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
