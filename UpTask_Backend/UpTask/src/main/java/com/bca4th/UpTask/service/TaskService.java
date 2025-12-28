package com.bca4th.UpTask.service;

import com.bca4th.UpTask.dto.TaskDto;
import com.bca4th.UpTask.model.Task;
import com.bca4th.UpTask.model.TaskStatus;
import com.bca4th.UpTask.model.User;
import com.bca4th.UpTask.repository.TaskRepository;
import com.bca4th.UpTask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskDto createTask(TaskDto TaskDto, String createdByEmail) {
        User createdBy = userRepository.findByEmail(createdByEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(TaskDto.getTitle());
        task.setDescription(TaskDto.getDescription());
        task.setStatus(TaskDto.getStatus() != null ? TaskDto.getStatus() : TaskStatus.TODO);
        task.setPriority(TaskDto.getPriority());
        task.setDueDate(TaskDto.getDueDate());
        task.setCreatedBy(createdBy);

        if (TaskDto.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(TaskDto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignedTo);
        }

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUserId(Long userId) {
        return taskRepository.findByAssignedToIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToDTO(task);
    }

    @Transactional
    public TaskDto updateTask(Long id, TaskDto TaskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(TaskDto.getTitle());
        task.setDescription(TaskDto.getDescription());
        task.setStatus(TaskDto.getStatus());
        task.setPriority(TaskDto.getPriority());
        task.setDueDate(TaskDto.getDueDate());

        if (TaskDto.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(TaskDto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignedTo);
        } else {
            task.setAssignedTo(null);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public TaskDto updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDto convertToDTO(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());

        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getName());
            dto.setAssignedToEmail(task.getAssignedTo().getEmail());
        }

        if (task.getCreatedBy() != null) {
            dto.setCreatedById(task.getCreatedBy().getId());
            dto.setCreatedByName(task.getCreatedBy().getName());
        }

        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        return dto;
    }
}