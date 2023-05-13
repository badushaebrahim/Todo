package training.doctor.management.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.doctor.management.enums.CountriesEnums;
import training.doctor.management.model.request.CreateUserRequest;
import training.doctor.management.model.request.LoginRequest;
import training.doctor.management.model.response.ErrorModel;
import training.doctor.management.model.response.UserCreatedResposposne;
import training.doctor.management.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final AuthService service;

    @CrossOrigin("*")
    @PostMapping("/user")
    public ResponseEntity<?> createNewDoctor(@RequestBody CreateUserRequest request, @RequestHeader("country") CountriesEnums countriesEnums) {

        try {
            return new ResponseEntity<>(service.createUser(request), HttpStatus.ACCEPTED);
        }
        catch(Exception e){
           return new  ResponseEntity<>(new ErrorModel(e.getMessage(),"NONE"),HttpStatus.BAD_REQUEST);
            }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createNewDoctor(@RequestBody LoginRequest request, @RequestHeader("country") CountriesEnums countriesEnums) {

        try {
            return new ResponseEntity<>(service.loginUser(request), HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            return new  ResponseEntity<>(new ErrorModel(e.getMessage(),"NONE"),HttpStatus.BAD_REQUEST);
        }
    }

}


