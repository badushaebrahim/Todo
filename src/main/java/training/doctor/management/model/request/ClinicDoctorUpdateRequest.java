package training.doctor.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ClinicDoctorUpdateRequest {
        private String id;
        private String clinicName;
        private String clinicCity;
        private String doctorName;
        private String consultaionFee;

    }
