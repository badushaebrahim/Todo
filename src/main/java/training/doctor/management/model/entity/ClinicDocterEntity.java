package training.doctor.management.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import training.doctor.management.enums.CountriesEnums;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clinic-doctor-lists")
public class ClinicDocterEntity {

    @Id
    private String id;

    private String clinicName;
    private String clinicCity;
    private String doctorName;
    private String consultaionFee;

    private CountriesEnums country;

    private LocalDateTime createdAt;
    private String createBy;

    private LocalDateTime lastModifiedAt;
    private String lastModifiedby;



    public void  setCurrencyPostfix() {
        switch (this.country.name()){
            case "JO":
                this.consultaionFee +=" JOD";
                break;
            case "UA":
                this.consultaionFee +=" UA";
                break;
            case "AE":
                this.consultaionFee +=" AE";
                break;
            case "EG":
                this.consultaionFee +=" ED";
                break;
            default:
                break;
        }


    }
}
