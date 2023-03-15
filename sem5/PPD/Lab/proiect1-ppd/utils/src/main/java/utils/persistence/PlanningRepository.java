package utils.persistence;

import utils.Container;
import utils.domain.Payment;
import utils.domain.Planning;
import utils.domain.Treatment;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PlanningRepository {
    private final JDBCUtils jdbcUtils;
    private final TreatmentRepository treatmentRepository;

    public PlanningRepository() {
        treatmentRepository = Container.treatmentRepository;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("D:\\Facultate\\PPD\\teme\\proiect1\\server\\src\\main\\resources\\database.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JDBCUtils(properties);
    }

    public Planning save(Planning planning) {
        Connection connection = jdbcUtils.getConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO planning (name, cnp, date, location, treatmentId, treatmentDate, hour) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, planning.getName());
            preparedStatement.setString(2, planning.getCnp());
            preparedStatement.setString(3, planning.getDate().format(dateTimeFormatter));
            preparedStatement.setInt(4, planning.getLocation());
            preparedStatement.setInt(5, planning.getTreatment().getId());
            preparedStatement.setString(6, planning.getTreatmentDate().format(dateTimeFormatter));
            preparedStatement.setInt(7, planning.getHour());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return findLast();
    }

    public Planning findLast() {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM planning ORDER BY id DESC LIMIT 1")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String cnp = resultSet.getString("cnp");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), sdf);
                Integer location = resultSet.getInt("location");
                Integer treatmentId = resultSet.getInt("treatmentId");
                LocalDateTime treatmentDate = LocalDateTime.parse(resultSet.getString("treatmentDate"), sdf);
                Integer hour = resultSet.getInt("hour");
                Treatment treatment = treatmentRepository.findAll().stream().filter(t -> treatmentId.equals(t.getId())).toList().get(0);

                return new Planning(id, name, cnp, date, location, treatment, treatmentDate, hour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Planning> findAll() {
        Connection connection = jdbcUtils.getConnection();
        List<Planning> plannings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM planning")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String cnp = resultSet.getString("cnp");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), sdf);
                Integer location = resultSet.getInt("location");
                Integer treatmentId = resultSet.getInt("treatmentId");
                LocalDateTime treatmentDate = LocalDateTime.parse(resultSet.getString("treatmentDate"), sdf);
                Integer hour = resultSet.getInt("hour");
                Treatment treatment = treatmentRepository.findAll().stream().filter(t -> treatmentId.equals(t.getId())).toList().get(0);

                Planning planning = new Planning(id, name, cnp, date, location, treatment, treatmentDate, hour);
                plannings.add(planning);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plannings;
    }

    public void deleteById(Integer planningId) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM planning WHERE id = ?")) {
            preparedStatement.setInt(1, planningId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
