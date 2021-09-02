package study.datajpa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomIdTest {

  @Test
  void testGenerateId() throws Exception {
    // given
    String id = CustomId.generate();

    // when

    // then

    System.out.println("id = " + id);
  }
}
