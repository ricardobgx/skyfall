package com.skyfall.tasks.api.task.controllers;

import com.skyfall.tasks.api.task.dtos.TaskDetails;
import com.skyfall.tasks.api.task.enums.TaskStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskController {
    @GetMapping
    public ResponseEntity<List<TaskDetails>> findAllTasks() {
        List<TaskDetails> taskDetailsList = List.of(
                new TaskDetails("Estudar Spring", TaskStatus.PENDING, Instant.now().plus(30L, ChronoUnit.DAYS)),
                new TaskDetails("Estudar React", TaskStatus.PENDING, Instant.now().plus(30L, ChronoUnit.DAYS)),
                new TaskDetails("Estudar MongoDB", TaskStatus.PENDING, Instant.now().plus(30L, ChronoUnit.DAYS))
        );

        return ResponseEntity.ok(taskDetailsList);
    }
}
