package training.doctor.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import training.doctor.management.enums.CountriesEnums;
import training.doctor.management.enums.TimeEnum;
import training.doctor.management.model.request.AppointmentRequest;
import training.doctor.management.model.request.ClinicDoctorRequest;
import training.doctor.management.model.request.ClinicDoctorUpdateRequest;
import training.doctor.management.model.request.DocterAvaliablityrequest;
import training.doctor.management.model.response.ResponseModel;
import training.doctor.management.service.AppointmentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
@Slf4j
@RestController

public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/doctors")
    public ResponseEntity<ResponseModel> createNewDoctor(@RequestBody ClinicDoctorRequest request, @RequestHeader("country") CountriesEnums countriesEnums) {
        try {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.createNewDoctor(request, countriesEnums)), null, "200"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }
    }

    @PutMapping("/doctors")
    public ResponseEntity<ResponseModel> updateDocter(@RequestBody ClinicDoctorUpdateRequest request,@RequestHeader("country") CountriesEnums countriesEnums) {
        try {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.updateDoctor(request, countriesEnums)), null, "200"), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }
    }@DeleteMapping("/doctors")
    public ResponseEntity<ResponseModel> deleteDoctor(@RequestParam("id")String  id) {
        try {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.deleteClinic(id)), null, "200"), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }
    }


    @GetMapping("/doctor-avaliablity")
    public ResponseEntity<ResponseModel> checkDoctorAvailablity(@RequestParam("doctorName") String doctorName ,@RequestParam("clinicName") String clinicName ,@RequestParam("clinicCity") String clinicCity ,@RequestParam("date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("appointmentTime") TimeEnum appointmentTime,@RequestParam("uuid") String uuid) {

        try {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.getDoctorAvailability(new DocterAvaliablityrequest(doctorName,clinicName,clinicCity,date,appointmentTime),uuid)), null, "200"), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }

    }

    @PostMapping("/booking")
    public ResponseEntity<ResponseModel> bookAppointment(@RequestBody AppointmentRequest request, @RequestHeader("country") CountriesEnums countriesEnums, @RequestHeader("uuid") String uuid) {
        try {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.makeBookings(request, countriesEnums, uuid)), null, "200"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }

    }


    @PutMapping("/update-booking")
    public ResponseEntity<ResponseModel> updateBooking(@RequestBody AppointmentRequest request, @RequestParam("id") String id,@RequestHeader("uuid") String uuid){
    try {
        return new ResponseEntity<>(new ResponseModel( Optional.ofNullable(appointmentService.updateBookings(request, id, uuid)), null, "200"), HttpStatus.ACCEPTED);
    } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete-booking")
    public ResponseEntity<ResponseModel> deleteBooking(@RequestParam("id") String id){
       try {
           return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.deleteAppointment(id)), null, "205"), HttpStatus.OK);
       }catch (Exception e) {
           return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
       }
       }

       @GetMapping("/doctors")
    public ResponseEntity<ResponseModel> getAllClinics(@RequestHeader("uuid") String uuid)
       {
           try {
               return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(appointmentService.getAllClinics(uuid)), null, "205"), HttpStatus.OK);
           }catch (Exception e) {
               return new ResponseEntity<>(new ResponseModel(Optional.ofNullable(null), e.getMessage(), "500"), HttpStatus.OK);
           }
       }


    @PostMapping("/uploadFile")
    public ResponseEntity<Object> uploadFile(@RequestParam("File") MultipartFile file) throws IOException {

        return new ResponseEntity<Object>(appointmentService.fileUploader(file), HttpStatus.OK);
    }

}
