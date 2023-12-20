import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    Connection connection;
    Scanner scanner;
    Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void viewDoctor() throws SQLException {
        String viewDoctorQuery = "SELECT * FROM DOCTOR";
        PreparedStatement preparedStatement = connection.prepareStatement(viewDoctorQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        int maxDoctorIdWidth = 9;
        int maxNameWidth = 21;
        int maxSpecializationWidth = 19;
        System.out.println("|---------------------------------------------------------|");
        System.out.println("|                   *** Doctor List ***                   |");
        System.out.println("|---------------------------------------------------------|");
        System.out.println("| Doctor ID | Doctor Name           | Specialization      |");
        System.out.println("|---------------------------------------------------------|");

        while (resultSet.next()) {
            int doctorId = resultSet.getInt("doctor_id");
            String name = resultSet.getString("name");
            String specialization = resultSet.getString("specialization");
            String formatedString = String.format("| %-" +maxDoctorIdWidth+"d | %-"+ maxNameWidth+ "s | %-" +maxSpecializationWidth+"s |" ,  doctorId , name , specialization);
            System.out.println(formatedString);
        }
        System.out.println("|---------------------------------------------------------|");
    }

    public boolean isDoctorExists(Connection connection, int doctorId) throws SQLException {
        String query = "SELECT doctor_id FROM DOCTOR WHERE doctor_id ="+doctorId;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
