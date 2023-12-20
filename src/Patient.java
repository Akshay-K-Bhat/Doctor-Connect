import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    Connection connection;
    Scanner scanner;
    Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Patient Name");
        String patientName = scanner.nextLine();
        System.out.println("Enter Age");
        int age = scanner.nextInt();
        System.out.println("Enter Gender");
        String gender = scanner.next().toUpperCase();
        String insertPatientQuery = "INSERT INTO PATIENT(patient_id, name, age, gender) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertPatientQuery);
        preparedStatement.setInt(1 , generatePatientId());
        preparedStatement.setString(2, patientName);
        preparedStatement.setInt(3, age);
        preparedStatement.setString(4, gender);
        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0) {
            System.out.println("Patient Added Successfully");
        }
        else {
            System.out.println("Failed");
        }
    }

    public void removePatient(Connection connection, Scanner scanner) throws Exception {
        viewPatient();
        System.out.println("Enter patient ID");
        int patientID = scanner.nextInt();

        if(!isPatientExists(connection,patientID)){
            System.out.println("Enter valid Patient ID");
            return;
        }

        String deletePatientQuery = "DELETE FROM PATIENT WHERE patient_id = "+patientID;
        PreparedStatement preparedStatement = connection.prepareStatement(deletePatientQuery);
        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0) {
            System.out.println("Deleted Successfully");
        }
        else {
            System.out.println("Deletion Failed");
        }
    }

    public void viewPatient() throws SQLException {
        String viewPatientQuery = "SELECT * FROM PATIENT";
        PreparedStatement preparedStatement = connection.prepareStatement(viewPatientQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("|-----------------------------------------------------|");
        System.out.println("| Patient ID | Patient Name          | Age | Gender   |");
        System.out.println("|---------------------------------------=-------------|");

        int maxPatientIdWidth = 10;
        int maxNameWidth = 21;
        int maxAgeWidth = 3;
        int maxGenderWidth = 8;

        while (resultSet.next()) {
            int patientId = resultSet.getInt("patient_id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");

            String formattedRow = String.format("| %-" + maxPatientIdWidth + "d | %-" + maxNameWidth + "s | %-" + maxAgeWidth + "d | %-" + maxGenderWidth + "s |",
                    patientId, name, age, gender);

            System.out.println(formattedRow);
            System.out.println("|-----------------------------------------------------|");
        }

    }

    public  int  generatePatientId() throws SQLException{
        String patientIdQuery = "SELECT patient_id FROM PATIENT ORDER BY patient_id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(patientIdQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            int newId = resultSet.getInt(1);
            return newId+1;
        }

        return 1000;
    }

    public boolean isPatientExists(Connection connection, int patientID) throws  SQLException {
        String query = "SELECT patient_id FROM PATIENT WHERE patient_id = "+patientID;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
