package com.todo.web.controller;

import com.todo.web.dto.TodoCreateDto;
import com.todo.web.dto.TodoUpdateDto;
import com.todo.web.model.Todo;
import com.todo.web.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping(value = "/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping("/{id}")
    public String getTodoById(@PathVariable("id") Long id, Model model) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if (optionalTodo.isEmpty()) {
            return "error";
        }
        Todo todo = optionalTodo.get();
        model.addAttribute("todo", todo);
        return "todo/details";
    }

    @GetMapping
    public String findAll(Model model) {
        List<Todo> todoList =  todoService.findAll();
        model.addAttribute("todos", todoList);
        return "todo/list";
    }

    @PostMapping
    public String createTodo(@ModelAttribute TodoCreateDto todoCreateDto) {
        Todo newTodo = new Todo(false, false, todoCreateDto.getDescription());
        todoService.save(newTodo);
        return "redirect:/todos";
    }

    @PostMapping("/{id}")
    public String updateTodoById(@PathVariable("id") Long id, @ModelAttribute TodoUpdateDto todoUpdateDto) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if (optionalTodo.isEmpty()) {
            return "error";
        }
        Todo todo = optionalTodo.get();
        todo.setStarred(todoUpdateDto.getStarred());
        todo.setCompleted(todoUpdateDto.getCompleted());
        todo.setDescription(todoUpdateDto.getDescription());

        todoService.save(todo);

        return "redirect:/todos";


    }

    @PostMapping("/update/{id}")
    public String patchTodoById(@PathVariable("id") Long id, @ModelAttribute TodoUpdateDto todoUpdateDto) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if (optionalTodo.isEmpty()) {
            return "error";
        }

        Todo todo = optionalTodo.get();
        if (todoUpdateDto.getStarred() != null) {
            todo.setStarred(todoUpdateDto.getStarred());
        }
        if (todoUpdateDto.getCompleted() != null) {
            todo.setCompleted(todoUpdateDto.getCompleted());
        }
        if (todoUpdateDto.getDescription() != null) {
            todo.setDescription(todoUpdateDto.getDescription());
        }
        todoService.save(todo);
        return "redirect:/todos";
    }


    @PostMapping("/delete/{id}")
    public String deleteTodoById(@PathVariable("id") Long id) {
        Optional<Todo> optionalTodo = todoService.findById(id);

        if (optionalTodo.isEmpty()) {
            return "error";
        }

        Todo todo = optionalTodo.get();

        todoService.delete(todo);
        return "redirect:/todos";
    }

}
