package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v1.ToDoDao;
import com.quicktutorialz.nio.daos.v1.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class ToDoEndpoints extends Endpoints {

    ToDoDao toDoDao = ToDoDaoImpl.getInstance();

    @Api(path = "/api/v1/create", method = "POST", consumes = "application/json", produces = "application/json")
    Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDoDto input = (ToDoDto) getDataFromJsonBodyRequest(request, ToDoDto.class);
        ToDo output = toDoDao.create(input);
        toJsonResponse(request, response, new ResponseDto(200, output));
    };

    @Api(path = "/api/v1/read/{id}", method = "GET", produces = "application/json")
    Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        final String id = getPathVariables(request).get("id");
        final Optional<ToDo> output = toDoDao.read(id);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "Not Found"));
    };

    @Api(path = "/api/v1/read", method = "GET", produces = "application/json", description = "")
    Action readAllToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        final List<ToDo> output = toDoDao.readAll();
        toJsonResponse(request, response, new ResponseDto(200, output));
    };

    @Api(path = "/api/v1/update/{id}", method = "POST", consumes = "application/json", produces = "application/json")
    Action updateToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDo input = (ToDo) getDataFromJsonBodyRequest(request, ToDo.class);
        final Optional<ToDo> output = toDoDao.update(input);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get() : "Not Found"));
    };

    @Api(path = "/api/v1/delete/{id}", method = "GET", produces = "application/json")
    Action deleteToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        final String id = getPathVariables(request).get("id");
        final Boolean deleted = toDoDao.delete(id);
        toJsonResponse(request, response, new ResponseDto(200, deleted ? "Deleted" : "Not Found"));
    };

    public ToDoEndpoints() {
        setEndpoint("/api/v1/create", createToDo);
        setEndpoint("/api/v1/read/{id}", readToDo);
        setEndpoint("/api/v1/read", readAllToDo);
        setEndpoint("/api/v1/update/{id}", updateToDo);
        setEndpoint("/api/v1/delete/{id}", deleteToDo);
    }
}
