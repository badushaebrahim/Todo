package training.doctor.management.exceptions;

public class AlreadyBookedException extends RuntimeException{
    public AlreadyBookedException(){
        super("The Doctor is Already booked");
    }
}
