package training.doctor.management.exceptions;

public class DoctorAlreadyBookedException extends RuntimeException{
    public DoctorAlreadyBookedException (){
        super("The Doctor is already booked for that time");
    }
}
