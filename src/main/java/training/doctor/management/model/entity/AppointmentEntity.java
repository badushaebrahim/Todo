package training.doctor.management.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import training.doctor.management.enums.GenderEnums;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "appointments")

public class AppointmentEntity {
    @Id
    private String patientId;

    private String patientName;
    private int patientAge;
    private GenderEnums patientGender;

    private String doctorName;
    private String clinicId;
    private String clinicName;
    private String clinicCity;

    private String consultationFee;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate appointmentDate;
    private String appointmentTime;

    private String nationalId;
    private String patientUuid;


    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifedBy;


}
