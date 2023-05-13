package training.doctor.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import training.doctor.management.enums.TimeEnum;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocterAvaliablityrequest {
    private String doctorName;
    private String clinicName;
    private String clinicCity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private TimeEnum appointmentTime;

}
