package ch.stoeckli.lionel.lostandfound.item;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ch.stoeckli.lionel.lostandfound.storage.EntityNotFoundException;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> getItems() {
        return repository.findAll();
    }

    public Item getItem(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Item.class));
    }

    public Item insertItem(@NonNull Item item) {
        item.setId(null);
        return repository.save(item);
    }

    public Item updateItem(@NonNull Item item, @NonNull Long id) {
        return repository.findById(id)
                .map(orig -> {
                    orig.setName(item.getName());
                    orig.setDescription(item.getDescription());
                    orig.setColor(item.getColor());
                    return repository.save(orig);
                })
                .orElseGet(() -> repository.save(item));
    }

    public void deleteItem(@NonNull Long id) {
        repository.deleteById(id);
    }
}
