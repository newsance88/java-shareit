package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id;
    String description;
    User requestor;
    LocalDateTime created;
}
