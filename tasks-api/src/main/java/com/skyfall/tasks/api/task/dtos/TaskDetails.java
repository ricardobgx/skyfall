package com.skyfall.tasks.api.task.dtos;

import com.skyfall.tasks.api.task.enums.TaskStatus;

import java.time.Instant;

public record TaskDetails(
        String title,
        TaskStatus status,
        Instant dueDate
) {
}
