package com.example.tasktrackerclidemo;

import com.example.tasktrackerclidemo.service.TaskService;
import com.example.tasktrackerclidemo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TasktrackerclidemoApplication implements CommandLineRunner {

	@Autowired
	private TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(TasktrackerclidemoApplication.class, args);
	}

	private void printUsage() {
		System.out.println("Usage:");
		System.out.println("  task-cli add \"<description>\"");
		System.out.println("  task-cli update <id> \"<new description>\"");
		System.out.println("  task-cli delete <id>");
		System.out.println("  task-cli mark-in-progress <id>");
		System.out.println("  task-cli mark-done <id>");
		System.out.println("  task-cli list");
		System.out.println("  task-cli list done");
		System.out.println("  task-cli list todo");
		System.out.println("  task-cli list in-progress");
	}

	@Override
	public void run(String... args) {
		if (args.length == 0) {
			printUsage();
		}

		try {
			String command = args[0];
			switch (command) {
				case "add":
					if (args.length < 2) throw new IllegalArgumentException("Description required");
					Task newTask = taskService.addTask(args[1]);
					System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
					break;

				case "update":
					if (args.length < 3) throw new IllegalArgumentException("ID and new description required");
					boolean updated = taskService.updateTask(Integer.parseInt(args[1]), args[2]);
					System.out.println(updated ? "Task updated successfully" : "Task not found");
					break;

				case "delete":
					if (args.length < 2) throw new IllegalArgumentException("ID required");
					boolean deleted = taskService.deleteTask(Integer.parseInt(args[1]));
					System.out.println(deleted ? "Task deleted successfully" : "Task not found");
					break;

				case "mark-in-progress":
					if (args.length < 2) throw new IllegalArgumentException("ID required");
					boolean markedInProgress = taskService.updateTaskStatus(Integer.parseInt(args[1]), "in-progress");
					System.out.println(markedInProgress ? "Task marked as in-progress" : "Task not found");
					break;

				case "mark-done":
					if (args.length < 2) throw new IllegalArgumentException("ID required");
					boolean markedDone = taskService.updateTaskStatus(Integer.parseInt(args[1]), "done");
					System.out.println(markedDone ? "Task marked as done" : "Task not found");
					break;

				case "list":
					String status = args.length > 1 ? args[1] : null;
					if (status != null && !Arrays.asList("done", "todo", "in-progress").contains(status)) {
						throw new IllegalArgumentException("Invalid status");
					}
					List<Task> tasks = taskService.listTasks(status);
					if (tasks.isEmpty()) {
						System.out.println("No tasks found");
					} else {
						tasks.forEach(task -> System.out.printf("ID: %d | Status: %s | Description: %s%n",
								task.getId(), task.getStatus(), task.getDescription()));
					}
					break;

				default:
					System.out.println("Unknown command: " + command);
					printUsage();
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid ID format. Please provide a valid number.");
			printUsage();
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			printUsage();
		} catch (Exception e) {
			System.err.println("Unexpected error: " + e.getMessage());
			printUsage();
		}

	}
}