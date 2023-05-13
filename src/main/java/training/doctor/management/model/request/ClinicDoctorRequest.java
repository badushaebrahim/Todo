package training.doctor.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import training.doctor.management.enums.CountriesEnums;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicDoctorRequest {
    private String clinicName;
    private String clinicCity;
    private String doctorName;
    private String consultaionFee;

   }
