package ch.stoeckli.lionel.lostandfound;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import ch.stoeckli.lionel.lostandfound.item.Item;
import ch.stoeckli.lionel.lostandfound.item.ItemRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
public class DBTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createItem() {
        Item item = this.itemRepository.save(new Item("Schlüssel", "Silberner Hausschlüssel", "silber"));
        Assertions.assertNotNull(item.getId());
    }

    @Test
    void readItem() {
        Item saved = this.itemRepository.save(new Item("Geldbeutel", "Schwarzer Geldbeutel", "schwarz"));
        Optional<Item> found = this.itemRepository.findById(saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Geldbeutel", found.get().getName());
    }

    @Test
    void updateItem() {
        Item saved = this.itemRepository.save(new Item("Brille", "Lesebrille", "schwarz"));
        saved.setColor("braun");
        Item updated = this.itemRepository.save(saved);
        Assertions.assertEquals("braun", updated.getColor());
    }

    @Test
    void deleteItem() {
        Item saved = this.itemRepository.save(new Item("Handy", "iPhone 12", "blau"));
        Long id = saved.getId();
        this.itemRepository.deleteById(id);
        Assertions.assertFalse(this.itemRepository.findById(id).isPresent());
    }

    @Test
    void listItems() {
        this.itemRepository.save(new Item("Buch", "Roman", null));
        this.itemRepository.save(new Item("Stift", "Kugelschreiber", "blau"));
        List<Item> items = this.itemRepository.findAll();
        Assertions.assertTrue(items.size() >= 2);
    }
}
