package com.quicktutorialz.nio.endpoints.v2;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v2.ToDoDao;
import com.quicktutorialz.nio.daos.v2.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import io.reactivex.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class ToDoEndpointsV2 extends Endpoints {

    ToDoDao toDoDao = ToDoDaoImpl.getInstance();

    @Api(path = "/api/v2/create", method = "POST", consumes = "application/json", produces = "application/json")
    Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .map(req -> (ToDoDto) getDataFromJsonBodyRequest(req, ToDoDto.class))
                .flatMap(toDoDto -> toDoDao.create(toDoDto))
                .subscribe(toDo -> toJsonResponse(request, response, new ResponseDto(200, toDo)));
    };

    @Api(path = "/api/v2/read/{id}", method = "GET", produces = "application/json")
    Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .map(req -> getPathVariables(request).get("id"))
                .flatMap(id -> toDoDao.read(id))
                .subscribe(output -> toJsonResponse(request, response,
                        new ResponseDto(200, output.isPresent() ? output.get() : "Not Found")));
    };

    @Api(path = "/api/v2/read", method = "GET", produces = "application/json")
    Action readAllToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .flatMap(id -> toDoDao.readAll())
                .subscribe(output -> toJsonResponse(request, response, new ResponseDto(200, output)),
                        throwable -> toJsonResponse(request, response, new ResponseDto(200, throwable))
                );
    };

    @Api(path = "/api/v2/update/{id}", method = "POST", consumes = "application/json", produces = "application/json")
    Action updateToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .map(req -> (ToDo) getDataFromJsonBodyRequest(request, ToDo.class))
                .flatMap(toDo -> toDoDao.update(toDo))
                .subscribe(output -> toJsonResponse(request, response,
                        new ResponseDto(200, output.isPresent() ? output.get() : "Not Found")));
    };

    @Api(path = "/api/v2/delete/{id}", method = "GET", produces = "application/json")
    Action deleteToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .map(req -> getPathVariables(request).get("id"))
                .flatMap(id -> toDoDao.delete(id))
                .subscribe(deleted -> toJsonResponse(request, response,
                        new ResponseDto(200, deleted ? "Deleted" : "Not Found")));
    };

    public ToDoEndpointsV2() {
        setEndpoint("/api/v2/create", createToDo);
        setEndpoint("/api/v2/read/{id}", readToDo);
        setEndpoint("/api/v2/read", readAllToDo);
        setEndpoint("/api/v2/update/{id}", updateToDo);
        setEndpoint("/api/v2/delete/{id}", deleteToDo);
    }
}
