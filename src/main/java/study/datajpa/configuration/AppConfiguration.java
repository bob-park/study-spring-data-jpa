package study.datajpa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing // ! JPA Auditing 기능 사용하 반드시 넣어야 한다.
public class AppConfiguration {

  /**
   * ! 생성자, 마지막 수정자를 JPA Auditing 을 사용할 경우 반드시 넣어야 한다.
   *
   * * 실제 사용시
   *
   * <pre>
   *     - Spring Security 을 사용할 경우 Security Context 에서 꺼내서 사용함
   *     - Session 사용 시 session 에서 얻어오면 됨
   * </pre>
   *
   *
   * @return
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of(UUID.randomUUID().toString());
  }
}
