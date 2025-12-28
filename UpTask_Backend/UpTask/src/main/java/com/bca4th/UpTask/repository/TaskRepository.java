package com.bca4th.UpTask.repository;

import com.bca4th.UpTask.model.Task;
import com.bca4th.UpTask.model.TaskStatus;
import com.bca4th.UpTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByCreatedBy(User user);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);

    List<Task> findAllByOrderByCreatedAtDesc();

    List<Task> findByAssignedToIdOrderByCreatedAtDesc(Long userId);
}
