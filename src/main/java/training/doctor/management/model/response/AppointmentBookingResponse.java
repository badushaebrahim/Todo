package training.doctor.management.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBookingResponse {

    private String clinicName;
    private String doctorName;
    private LocalDate bookedDate;
    private String fee;
    private String timeFrame;
    private String status;
    private String id;


}
