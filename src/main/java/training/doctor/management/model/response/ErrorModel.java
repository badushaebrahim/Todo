package training.doctor.management.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorModel {
    private String errorMsg;
    private String actionRequired;

}
