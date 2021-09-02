package study.datajpa.repository;

/**
 * 중첩 Projection
 *
 * <pre>
 *     - 중첩되는 entity 는 left outer join 이 실행된다.
 *     - 중첩되는 interface 는 entity 의 전체를 가져온다.
 *     - 중첩되는 순간 최적화가 불가능
 *     - 복잡한 쿼리에 한계가 있다.
 * </pre>
 */
public interface NestedClosedProjections {

  String getUsername();

  TeamInfo getTeam();

  interface TeamInfo {
    String getName();
  }
}
