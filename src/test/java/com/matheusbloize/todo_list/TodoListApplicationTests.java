package com.matheusbloize.todo_list;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.matheusbloize.todo_list.models.TodoModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TodoListApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	private TodoModel createTodo() {
		return new TodoModel("test todo", "test todo description", false, 1);
	}

	private List<TodoModel> getTodos() {
		return webTestClient.get()
				.uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(TodoModel.class)
				.returnResult()
				.getResponseBody();
	}

	private void deleteTodos() {
		List<TodoModel> todos = getTodos();
		for (int i = 0; i < todos.size(); i++) {
			webTestClient
					.delete()
					.uri("/todos/" + todos.get(i).getId())
					.exchange();
		}
	}

	@Test
	void testCreateTodoSuccess() {
		deleteTodos();
		var todo = createTodo();
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$[0].name").isEqualTo(todo.getName())
				.jsonPath("$[0].description").isEqualTo(todo.getDescription())
				.jsonPath("$[0].done").isEqualTo(todo.isDone())
				.jsonPath("$[0].priority").isEqualTo(todo.getPriority());
	}

	@Test
	void testCreateTodoFailure() {
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(new TodoModel("", "", false, 0))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void testGetTodos() {
		List<TodoModel> todos = getTodos();
		assertNotNull(todos);
		assertFalse(!todos.isEmpty());
	}

	@Test
	void testUpdateTodo() {
		deleteTodos();
		var todo = createTodo();
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange();
		List<TodoModel> response = getTodos();
		if (response != null && !response.isEmpty()) {
			int responseLength = response.size();
			TodoModel firstTodo = response.get(0);
			String updatedDescription = "todo description updated";
			webTestClient
					.put()
					.uri("/todos/" + firstTodo.getId())
					.bodyValue(new TodoModel(firstTodo.getName(), updatedDescription, firstTodo.isDone(), firstTodo.getPriority()))
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$").isArray()
					.jsonPath("$.length()").isEqualTo(responseLength)
					.jsonPath("$[0].id").isEqualTo(firstTodo.getId())
					.jsonPath("$[0].name").isEqualTo(todo.getName())
					.jsonPath("$[0].description").isEqualTo(updatedDescription)
					.jsonPath("$[0].done").isEqualTo(todo.isDone())
					.jsonPath("$[0].priority").isEqualTo(todo.getPriority());
		}
	}

	@Test
	void testDeleteTodo() {
		var todo = createTodo();
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange();
		List<TodoModel> response = getTodos();
		if (response != null && !response.isEmpty()) {
			int responseLength = response.size();
			webTestClient
					.delete()
					.uri("/todos/" + response.get(0).getId())
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$").isArray()
					.jsonPath("$.length()").isEqualTo(responseLength - 1);
		}
	}
}
