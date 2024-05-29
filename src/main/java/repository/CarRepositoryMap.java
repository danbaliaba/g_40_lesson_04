package repository;

import domain.Car;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepositoryMap implements CarRepository{

    private Map<Long, Car> database = new HashMap<Long, Car>();
    private long currentId;

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(currentId, car);
        return car;
    }

    @Override
    public Car getById(Long id) {
        return database.get(id);
    }

    @Override
    public List<Car> getAll() {
        return database.values().stream().toList();
    }

    @Override
    public Car update(Car car) {
        database.replace(car.getId(), car);
        return database.get(car.getId());
    }

    @Override
    public void delete(Long id) {
        database.remove(id);
    }
}
