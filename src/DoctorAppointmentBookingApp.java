import java.sql.*;
import java.util.Scanner;

public class DoctorAppointmentBookingApp {
    private static final String url = "jdbc:mysql://localhost:3306/doctor_connect";
    private static final String username = "root";
    private static final String password = "VIKINGastro@17";

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url , username , password);
        Scanner scanner = new Scanner(System.in);
        Doctor doctor = new Doctor(connection , scanner);
        Patient patient = new Patient(connection , scanner);
        while (true) {
            System.out.println();
            System.out.println("|-----------------------------------|");
            System.out.println("| ***       Doctor Connect      *** |");
            System.out.println("|-----------------------------------|");
            System.out.println("| 1.Add Patient                     |");
            System.out.println("| 2.Remove Patient                  |");
            System.out.println("| 3.View Patients                   |");
            System.out.println("| 4.View Doctors                    |");
            System.out.println("| 5.Book Appointment                |");
            System.out.println("| 6.View Appointments               |");
            System.out.println("| 7.Delete Appointment              |");
            System.out.println("| 8.Exit                            |");
            System.out.println("|-----------------------------------|");

            System.out.print("Enter choice -> ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> patient.addPatient();
                case 2 -> patient.removePatient(connection, scanner);
                case 3 -> patient.viewPatient();
                case 4 -> doctor.viewDoctor();
                case 5 -> bookAppointment(connection, scanner, doctor, patient);
                case 6 -> viewAppointments(connection);
                case 7 -> deleteAppointment(connection , scanner);
                case 8 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public static void bookAppointment(Connection connection , Scanner scanner , Doctor doctor , Patient patient) throws SQLException {
        int appointmentId = generateAppointmentId(connection);
        System.out.println("Enter Patient ID");
        int patientId = scanner.nextInt();

        if(!patient.isPatientExists(connection , patientId)) {
            System.out.println("No patient Exist with this ID , Please Enter Valid ID");
            return;
        }

        System.out.println("Enter Doctor ID ");
        int doctorId = scanner.nextInt();

        if(!doctor.isDoctorExists(connection, doctorId))
        {
            System.out.println("No doctor Exist with this ID , Please Enter Valid ID");
            return;
        }


        System.out.println("Enter Appointment Date (YYYY-MM-DD) : ");
        String date = scanner.next();

        if(doctorIsAvailable(connection , date , doctorId)) {
            String bookAppointmentQuery = "INSERT INTO APPOINTMENT VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(bookAppointmentQuery);
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.setInt(2, patientId);
            preparedStatement.setInt(3, doctorId);
            preparedStatement.setString(4, date);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected > 0) {
                System.out.println("Appointment Booked Successfully");
            }
        }
        else {
            System.out.println("Sorry Doctor already has Appointment on this Date");
        }
    }

    public static boolean doctorIsAvailable(Connection connection, String  date , int doctorId) throws SQLException {
        String checkDoctorQuery = "SELECT appointment_id FROM APPOINTMENT WHERE appointment_date = '" + date + "' AND doctor_id = " + doctorId;
        PreparedStatement preparedStatement = connection.prepareStatement(checkDoctorQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        return !resultSet.next();
    }

    public static int  generateAppointmentId(Connection connection) throws SQLException{
        String patientIdQuery = "SELECT appointment_id FROM APPOINTMENT ORDER BY appointment_id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(patientIdQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            int newId = resultSet.getInt(1);
            return newId+1;
        }

        return 3000;
    }

    public static void viewAppointments(Connection connection) throws SQLException {
        String viewAppointmentQuery = "SELECT appointment_id, appointment_date, patient_id, doctor_id FROM APPOINTMENT";
        PreparedStatement preparedStatement = connection.prepareStatement(viewAppointmentQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("|----------------------------------------------------------------------------------------------------|");
        System.out.println("|                                         *** Appointments ***                                       |");
        System.out.println("|----------------------------------------------------------------------------------------------------|");
        System.out.println("| Appointment ID | Patient Name      | Patient ID | Doctor Name       | Doctor ID | Appointment Date |");
        System.out.println("|----------------------------------------------------------------------------------------------------|");

        while (resultSet.next()) {
            int appointmentId = resultSet.getInt("appointment_id");
            int patientId = resultSet.getInt("patient_id");
            int doctorId = resultSet.getInt("doctor_id");
            String appointmentDate = resultSet.getString("appointment_date");

            String patientNameQuery = "SELECT NAME FROM PATIENT WHERE patient_id = ?";
            String doctorNameQuery = "SELECT NAME FROM DOCTOR WHERE doctor_id = ?";
            PreparedStatement patientNameStatement = connection.prepareStatement(patientNameQuery);
            PreparedStatement doctorNameStatement = connection.prepareStatement(doctorNameQuery);
            patientNameStatement.setInt(1, patientId);
            doctorNameStatement.setInt(1, doctorId);
            ResultSet patientNameResult = patientNameStatement.executeQuery();
            ResultSet doctorNameResult = doctorNameStatement.executeQuery();

            String patientName = patientNameResult.next() ? patientNameResult.getString(1) : "N/A";
            String doctorName = doctorNameResult.next() ? doctorNameResult.getString(1) : "N/A";


            int maxAppointmentIdWidth = 14;
            int maxPatientNameWidth = 17;
            int maxPatientIdWidth = 10;
            int maxDoctorNameWidth = 17;
            int maxDoctorIdWidth = 9;
            int maxAppointmentDateWidth = 16;

            String formattedString = String.format("| %-"+maxAppointmentIdWidth+"d | %-"+maxPatientNameWidth+"s | %-"+maxPatientIdWidth+"d | %-"+maxDoctorNameWidth+"s | %-"+maxDoctorIdWidth+"d | %-"+maxAppointmentDateWidth+"s |",
                        appointmentId, patientName, patientId, doctorName, doctorId, appointmentDate);

            System.out.println(formattedString);
            System.out.println("|----------------------------------------------------------------------------------------------------|");
        }
    }

    public static void deleteAppointment(Connection connection, Scanner scanner) throws SQLException {
        viewAppointments(connection);
        System.out.println("Enter Appointment ID");
        int appointmentId = scanner.nextInt();
        if(!isAppointmentExist(connection, appointmentId)) {
            System.out.println("Please Enter valid Appointment ID");
            return;
        }
        String query = "DELETE FROM APPOINTMENT WHERE appointment_id = "+appointmentId;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0) {
            System.out.println("Appointment canceled successfully");
        }
        else {
            System.out.println("Cancellation failed");
        }
    }

    public static boolean isAppointmentExist(Connection connection , int appointmentID) throws  SQLException {
        String query = "SELECT appointment_id FROM APPOINTMENT WHERE appointment_id = "+appointmentID;
        PreparedStatement preparedStatement  = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return  resultSet.next();
    }
}


