package utils.persistence;

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

public class PaymentRepository {
    private final JDBCUtils jdbcUtils;

    public PaymentRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("D:\\Facultate\\PPD\\teme\\proiect1\\server\\src\\main\\resources\\database.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JDBCUtils(properties);
    }

    public Payment save(Payment payment){
        Connection connection = jdbcUtils.getConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO payment (value, cnp, date, planningId) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, payment.getValue());
            preparedStatement.setString(2, payment.getCnp());
            preparedStatement.setString(3, payment.getDate().format(dateTimeFormatter));
            preparedStatement.setInt(4, payment.getPlanningId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return findLast();
    }

    public Payment findLast() {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payment ORDER BY id DESC LIMIT 1")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Integer id = resultSet.getInt("id");
                Integer value = resultSet.getInt("value");
                Integer planningId = resultSet.getInt("planningId");
                String cnp = resultSet.getString("cnp");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), sdf);

                return new Payment(id, value, cnp, date, planningId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Payment> findAll(){
        Connection connection = jdbcUtils.getConnection();
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payment")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                Integer id = resultSet.getInt("id");
                Integer value = resultSet.getInt("value");
                Integer planningId = resultSet.getInt("planningId");
                String cnp = resultSet.getString("cnp");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), sdf);

                Payment payment = new Payment(id, value, cnp, date, planningId);
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }
}
