package ch.stoeckli.lionel.lostandfound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.stoeckli.lionel.lostandfound.item.Item;
import ch.stoeckli.lionel.lostandfound.item.ItemRepository;
import ch.stoeckli.lionel.lostandfound.item.ItemService;

import static org.mockito.Mockito.*;

public class ItemServiceTest {

    private ItemService itemService;
    private final ItemRepository itemRepositoryMock = mock(ItemRepository.class);

    private final Item itemMock = mock(Item.class);

    @BeforeEach
    void setUp() {
        itemService = new ItemService(itemRepositoryMock);
    }

    @Test
    void createItem() {
        when(itemRepositoryMock.save(itemMock)).thenReturn(itemMock);
        itemService.insertItem(itemMock);
        verify(itemRepositoryMock, times(1)).save(any());
    }

    @Test
    void getAllItems() {
        itemService.getItems();
        verify(itemRepositoryMock, times(1)).findAll();
    }

    @Test
    void deleteItem() {
        itemService.deleteItem(1L);
        verify(itemRepositoryMock, times(1)).deleteById(any());
    }
}
