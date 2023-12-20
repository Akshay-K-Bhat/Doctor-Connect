# Doctor Connect - Appointment Booking Application

## Overview

Doctor Connect is a simple Java console application that allows users to manage appointments between patients and doctors. The application uses a MySQL database to store information about patients, doctors, and appointments. Users can perform various operations such as adding patients, removing patients, viewing patients, viewing doctors, booking appointments, viewing appointments, cancelling appointments, and exiting the application.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java Development Kit (JDK)
- MySQL Server
- MySQL Connector/J (JDBC Driver)

## Setup

1. **Database Setup:**
   - Create a MySQL database named `doctor_connect`.
   - Update the `url`, `username`, and `password` fields in the `DoctorAppointmentBookingApp` class with your MySQL connection details.

2. **Java Project Setup:**
   - Compile and run the `DoctorAppointmentBookingApp` class.

## Usage

1. Run the application, and you will see a menu with various options.
2. Choose an option by entering the corresponding number.
3. Follow the prompts to perform operations such as adding/removing patients, viewing patients/doctors, booking/canceling appointments, and exiting the application.

## Features

- **Add Patient:** Add a new patient to the database.
- **Remove Patient:** Remove a patient from the database.
- **View Patients:** Display a list of all patients.
- **View Doctors:** Display a list of all doctors.
- **Book Appointment:** Schedule an appointment between a patient and a doctor.
- **View Appointments:** Display a list of all scheduled appointments.
- **Delete Appointment:** Cancel a previously scheduled appointment.
- **Exit:** Terminate the application.

## Database Schema

The application uses the following database tables:

- **PATIENT:** Stores information about patients.
- **DOCTOR:** Stores information about doctors.
- **APPOINTMENT:** Stores information about scheduled appointments.

## Contributors

- Akshay-K-Bhat

## License

This project is licensed under the [MIT License](LICENSE).
