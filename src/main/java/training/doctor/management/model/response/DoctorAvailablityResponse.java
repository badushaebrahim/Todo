package training.doctor.management.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import training.doctor.management.enums.TimeEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailablityResponse {
    private String doctorName;
    private String clinicName;
    private String clinicCity;
    private String appointmentTime;

    private List<String> availableTimes;
    private List<String> bookedTimes;

    private String consultationFees;

    private String doctorStatus;


}
