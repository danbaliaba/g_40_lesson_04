package repository;

import constants.Constants;
import domain.Car;

import javax.swing.plaf.nimbus.State;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.*;

public class CarRepositoryDB implements CarRepository {

    private Connection getConnection() {

        try {
            Class.forName(DB_DRIVER_PATH);

            // jdbc:postgresql://localhost:5432/g_40_cars?user=postgres&password=qwerty007
            String dbUrl = String.format("%s%s?user=%s&password=%s", Constants.DB_ADDRESS, DB_NAME, DB_USERNAME, DB_PASSWORD);

            return DriverManager.getConnection(dbUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car save(Car car) {
        try (Connection connection = getConnection()) {
            String query = String.format("INSERT INTO car(brand, price, year) VALUES ('%s', %s, %d);",
                    car.getBrand(), car.getPrice(), car.getYear());

            Statement statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            Long id = resultSet.getLong(1);
            car.setId(id);

            return car;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getById(Long id) {
        try (Connection connection = getConnection()) {
            String query = String.format("SELECT * FROM car WHERE id = %d", id);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            String brand = resultSet.getString("brand");
            BigDecimal price = resultSet.getBigDecimal("price");
            int year = resultSet.getInt("year");

            return new Car(id, brand, price, year);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getAll() {
        try(Connection connection = getConnection()){
            String query = String.format("SELECT * FROM car;");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Car> cars = new ArrayList<>();
            while(resultSet.next()){
                String brand = resultSet.getString("brand");
                BigDecimal price = resultSet.getBigDecimal("price");
                int year = resultSet.getInt("year");
                Long id = resultSet.getLong("id");

                Car car = new Car(id, brand, price, year);

                cars.add(car);
            }
            return cars;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car update(Car car) {
        try (Connection connection = getConnection()) {
            String query = String.format("UPDATE car SET price=%s WHERE id=%d",
                    car.getPrice(), car.getId());
            Statement statement = connection.createStatement();
            statement.execute(query);

            return car;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = getConnection()) {

            String query = String.format("DELETE FROM car WHERE id = %d", id);
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
