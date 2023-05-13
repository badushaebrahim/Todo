package training.doctor.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import training.doctor.management.enums.GenderEnums;
import training.doctor.management.enums.TimeEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    private String patientName;
    private int patientAge;
    private GenderEnums gender;
    private String patientNationalId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

    private LocalDate appointmentDate;
    private TimeEnum appointmentTime;
    private String doctorName;
    private String clinicName;
    private String clinicCity;




}
