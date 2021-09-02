package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

  @Autowired private ItemRepository itemRepository;

  @Test
  void testItem() throws Exception {
    // given
    Item item = new Item();

    item.setId("1");

    itemRepository.save(item);

    // when

    // then
  }
}
