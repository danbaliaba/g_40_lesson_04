package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.CarRepository;
import repository.CarRepositoryDB;
import repository.CarRepositoryMap;
import service.CarService;
import service.CarServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarServlet extends HttpServlet {

    private CarService service = new CarServiceImpl(new CarRepositoryDB());

    // Получение автомобиля или всех автомобилей
    // GET http://localhost:8080/cars?id=3 - один авто
    // GET http://localhost:8080/cars - все авто

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // req - объект запроса, он содержит всю информацию которую прислал нам клиент
        // resp - объект ответа, который будет содержать информацию после того как отработал метод

        Map<String, String[]> params = req.getParameterMap();

        if (params.isEmpty()){
            List<Car> list = service.getAll();
            resp.getWriter().write(list.toString());
        }
        else {
            Long id = Long.parseLong(params.get("id")[0]);
            Car car = service.getById(id);
            resp.getWriter().write(car.toString());
        }
    }


    // Сохранение автомобиля в БД:
    // POST http://localhost:8080/cars - в теле будет приходить объект автомобиля

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        service.save(car);

        resp.getWriter().write(car.toString());
    }


    // Изменения уже существующего в БД автомобиля:
    // PUT http://localhost:8080/cars - в теле будет приходить объект автомобиля, подлежащий изменению

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        service.update(car);

        resp.getWriter().write(car.toString());
    }


    // Удаления автомобиля из БД:
    // DELETE http://localhost:8080/cars?id=3


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        service.delete(id);
        resp.getWriter().write("Car deleted");
    }
}
