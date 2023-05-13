package training.doctor.management.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorBookingResponse {
    private String appointmentId;
    private String clinicName;
    private String clinicCity;
    private String doctorName;

    private LocalDate date;

    private String timeFrame;
}
