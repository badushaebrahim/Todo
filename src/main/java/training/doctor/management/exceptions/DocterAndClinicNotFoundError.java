package training.doctor.management.exceptions;

public class DocterAndClinicNotFoundError extends RuntimeException{
    public DocterAndClinicNotFoundError(){
        super("Doctor or Clinic Not found");
    }
}
