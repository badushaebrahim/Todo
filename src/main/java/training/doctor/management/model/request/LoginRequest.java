package training.doctor.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String userName;
    private String password;

}
