package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        log.info("Item получен: {}", item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long id) {
        User owner = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);
        log.info("Item добавлен: {}", item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long userId) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!itemToUpdate.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this item");
        }
        if (itemDto.getName() != null) {
            itemToUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(itemToUpdate);
        log.info("Item обновлен: {}", updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        log.info("Items for user {}: {}", userId, items);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchByText(text);
        log.info("Items for text '{}': {}", text, items);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
