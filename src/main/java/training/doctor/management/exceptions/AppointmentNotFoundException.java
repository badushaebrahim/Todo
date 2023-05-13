package training.doctor.management.exceptions;

public class AppointmentNotFoundException extends  RuntimeException{
    public AppointmentNotFoundException(){

        super("Appointment Not found");
    }
}
