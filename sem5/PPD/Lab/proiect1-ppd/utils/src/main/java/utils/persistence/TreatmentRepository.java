package utils.persistence;

import utils.domain.Treatment;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TreatmentRepository {
    private final JDBCUtils jdbcUtils;

    public TreatmentRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("D:\\Facultate\\PPD\\teme\\proiect1\\server\\src\\main\\resources\\database.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JDBCUtils(properties);
    }

    public void save(Treatment treatment){
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO treatment (cost, duration) VALUES (?, ?)")) {
            preparedStatement.setInt(1, treatment.getCost());
            preparedStatement.setInt(2, treatment.getDuration());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Treatment> findAll(){
        Connection connection = jdbcUtils.getConnection();
        List<Treatment> treatmentList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM treatment")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer cost = resultSet.getInt("cost");
                Integer duration = resultSet.getInt("duration");

                Treatment treatment = new Treatment(id, cost, duration);
                treatmentList.add(treatment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return treatmentList;
    }

    public Treatment findById(Integer treatmentId) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM treatment WHERE id = ?")) {
            preparedStatement.setInt(1, treatmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer cost = resultSet.getInt("cost");
                Integer duration = resultSet.getInt("duration");

                return new Treatment(id, cost, duration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
