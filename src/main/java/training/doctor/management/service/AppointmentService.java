package training.doctor.management.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import training.doctor.management.enums.CountriesEnums;
import training.doctor.management.exceptions.AlreadyBookedException;
import training.doctor.management.exceptions.AppointmentNotFoundException;
import training.doctor.management.exceptions.DocterAndClinicNotFoundError;
import training.doctor.management.exceptions.DoctorAlreadyBookedException;
import training.doctor.management.model.DocDTO;
import training.doctor.management.model.entity.AppointmentEntity;
import training.doctor.management.model.entity.ClinicDocterEntity;
import training.doctor.management.model.request.AppointmentRequest;
import training.doctor.management.model.request.ClinicDoctorRequest;
import training.doctor.management.model.request.ClinicDoctorUpdateRequest;
import training.doctor.management.model.request.DocterAvaliablityrequest;
import training.doctor.management.model.response.AppointmentBookingResponse;
import training.doctor.management.model.response.DoctorAvailablityResponse;
;
import training.doctor.management.repo.AppointmentRepo;
import training.doctor.management.repo.ClinicAndDoctorRepo;
import training.doctor.management.service.helper.StorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static training.doctor.management.constants.AvailableTimes.APPONTMENT_SHEDULES;

@Slf4j
@Service
public class AppointmentService {
  @Autowired AppointmentRepo appointmentRepo;
  @Autowired ClinicAndDoctorRepo clinicAndDoctorRepo;
  @Autowired MongoTemplate mongoTemplate;
  @Autowired ModelMapper modelMapper;
@Autowired
  StorageService storage;
  public DoctorAvailablityResponse getDoctorAvailability(
      DocterAvaliablityrequest request, String uuid) {
    try {
      log.info("doctor request {} ", uuid);
      String doctorStats = "Available for the requested time";
      ClinicDocterEntity details =
          getClinicAndDoctorDetail(
              request.getClinicName(), request.getClinicCity(), request.getDoctorName());
      if (details == null) {
        log.error("doctor request {} ", uuid);
        throw new DocterAndClinicNotFoundError();
      }
      List<AppointmentEntity> datas =
          getDoctorReservation(
              request.getDoctorName(),
              request.getClinicName(),
              request.getClinicCity(),
              request.getDate());
      List<String> bookedTimes = new ArrayList<>();
      List<String> available = new ArrayList<>();
      for (AppointmentEntity data : datas) {
        bookedTimes.add(data.getAppointmentTime());
      }
      for (String time : APPONTMENT_SHEDULES) {
        if (!bookedTimes.contains(time)) {
          available.add(time);
        }
      }
      if (bookedTimes.contains(request.getAppointmentTime().convertAppointmentTimeToString())) {
        doctorStats =
            "Doctor is Alread booked for {} plase try Booking at some Available time"
                + request.getAppointmentTime().convertAppointmentTimeToString();
      }
      return new DoctorAvailablityResponse(
          details.getDoctorName(),
          details.getClinicName(),
          details.getClinicCity(),
          request.getAppointmentTime().convertAppointmentTimeToString(),
          available,
          bookedTimes,
          details.getConsultaionFee(),
          doctorStats);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public AppointmentBookingResponse makeBookings(
      AppointmentRequest request, CountriesEnums countriesEnums, String uuid) {
    DoctorAvailablityResponse data =
        getDoctorAvailability(
            new DocterAvaliablityrequest(
                request.getDoctorName(),
                request.getClinicName(),
                request.getClinicCity(),
                request.getAppointmentDate(),
                request.getAppointmentTime()),
            uuid);
    List<String> bookedTimes = data.getBookedTimes();
    if (bookedTimes.contains(request.getAppointmentTime().convertAppointmentTimeToString())) {
      throw new AlreadyBookedException();
    }
    AppointmentEntity appointment = modelMapper.map(request, AppointmentEntity.class);
    appointment.setAppointmentTime(request.getAppointmentTime().convertAppointmentTimeToString());

    appointment.setPatientUuid(uuid);
    appointment.setCreateBy(request.getPatientName());
    appointment.setCreatedAt(LocalDateTime.now());
    appointment.setPatientGender(request.getGender());
    appointment.setConsultationFee(data.getConsultationFees());
    appointment.setCreateBy(request.getPatientName());
    appointment.setCreatedAt(LocalDateTime.now());
    appointment.setPatientId(null);

    try {
      AppointmentEntity reponse = appointmentRepo.save(appointment);
      return new AppointmentBookingResponse(
          reponse.getClinicName(),
          reponse.getDoctorName(),
          reponse.getAppointmentDate(),
          reponse.getConsultationFee(),
          reponse.getAppointmentTime(),
          "Booked",
          reponse.getPatientId());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public Object updateBookings(AppointmentRequest request, String id, String uuid) {
    try {
      AppointmentEntity updatedAppointment = new AppointmentEntity();
      AppointmentEntity data =
          appointmentRepo.findById(id).orElseThrow(AppointmentNotFoundException::new);

      String patientName =
          StringUtils.isBlank(request.getPatientName())
              ? data.getPatientName()
              : request.getPatientName();
      updatedAppointment.setPatientName(patientName);
      int patientAge =
          StringUtils.isBlank(String.valueOf(request.getPatientAge()))
              ? data.getPatientAge()
              : request.getPatientAge();
      updatedAppointment.setPatientAge(patientAge);
      String doctorName =
          StringUtils.isBlank(request.getDoctorName())
              ? data.getDoctorName()
              : request.getDoctorName();
      updatedAppointment.setDoctorName(doctorName);
      String clinicName =
          StringUtils.isBlank(request.getClinicName())
              ? data.getClinicName()
              : request.getClinicName();
      updatedAppointment.setClinicName(clinicName);
      String clinicCity =
          StringUtils.isBlank(request.getClinicCity())
              ? data.getClinicCity()
              : request.getClinicCity();
      updatedAppointment.setNationalId(request.getPatientNationalId());
      updatedAppointment.setAppointmentDate(request.getAppointmentDate());
      updatedAppointment.setPatientGender((request.getGender()));
      updatedAppointment.setAppointmentTime(
          request.getAppointmentTime().convertAppointmentTimeToString());

      DoctorAvailablityResponse doctor =
          getDoctorAvailability(
              new DocterAvaliablityrequest(
                  request.getDoctorName(),
                  request.getClinicName(),
                  request.getClinicCity(),
                  request.getAppointmentDate(),
                  request.getAppointmentTime()),
              uuid);
      if (doctor.getDoctorStatus().equals("Available for the requested time")
          || ((updatedAppointment.getDoctorName().equals(doctor.getDoctorName()))
              && (updatedAppointment.getClinicName().equals(doctor.getClinicName()))
              && (updatedAppointment.getAppointmentTime().equals(doctor.getAppointmentTime())))) {
        updatedAppointment.setPatientId(id);
        updatedAppointment.setAppointmentTime(
            request.getAppointmentTime().convertAppointmentTimeToString());
        updatedAppointment.setConsultationFee(doctor.getConsultationFees());
        updatedAppointment.setLastModifiedAt(LocalDateTime.now());
        updatedAppointment.setLastModifedBy(request.getPatientName());
        updatedAppointment.setPatientUuid(uuid);
        updatedAppointment.setNationalId(request.getPatientNationalId());
        updatedAppointment.setClinicCity(clinicCity);
        return appointmentRepo.save(updatedAppointment);
      } else {
        return doctor;
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public String deleteAppointment(String id) {
    appointmentRepo.findById(id).orElseThrow(AppointmentNotFoundException::new);
    return "Deleted " + id;
  }

  public String deleteClinic(String id) {
    clinicAndDoctorRepo.findById(id).orElseThrow(AppointmentNotFoundException::new);
    return "Deleted " + id;
  }

  // general purpose function to get doctor reuse
  public ClinicDocterEntity getClinicAndDoctorDetail(
      String clinicName, String clinicCity, String doctorName) {
    Query query = new Query();
    query.addCriteria(Criteria.where("clinicName").is(clinicName));
    query.addCriteria(Criteria.where("doctorName").is(doctorName));
    query.addCriteria(Criteria.where("clinicCity").is(clinicCity));
    return mongoTemplate.findOne(query, ClinicDocterEntity.class);
  }

  public List<AppointmentEntity> getDoctorReservation(
      String doctorName, String clinicName, String clinicCity, LocalDate appointmentDate) {
    Query query = new Query();
    query.addCriteria(Criteria.where("doctorName").is(doctorName));
    query.addCriteria(Criteria.where("clinicName").is(clinicName));
    query.addCriteria(Criteria.where("clinicCity").is(clinicCity));
    query.addCriteria(Criteria.where("appointmentDate").is(appointmentDate));
    return mongoTemplate.find(query, AppointmentEntity.class);
  }

  public ClinicDocterEntity createNewDoctor(
      ClinicDoctorRequest request, CountriesEnums countriesEnums) {

    ClinicDocterEntity data = modelMapper.map(request, ClinicDocterEntity.class);
    data.setId(null);
    data.setCountry(countriesEnums);
    data.setConsultaionFee(request.getConsultaionFee());
    data.setCurrencyPostfix();
    data.setCreateBy("admin");
    data.setCreatedAt(LocalDateTime.now());
    data.setLastModifiedby("admin");
    data.setLastModifiedAt(LocalDateTime.now());
    return clinicAndDoctorRepo.save(data);
  }

  public ClinicDocterEntity updateDoctor(
      ClinicDoctorUpdateRequest request, CountriesEnums countriesEnums) {
    ClinicDocterEntity data =
        clinicAndDoctorRepo
            .findById(request.getId())
            .orElseThrow(DoctorAlreadyBookedException::new);
    ClinicDocterEntity update = new ClinicDocterEntity();
    String clinicName =
        StringUtils.isBlank(request.getClinicName())
            ? data.getClinicName()
            : request.getClinicName();
    update.setClinicName(clinicName);
    String clinicCity =
        StringUtils.isBlank(request.getClinicCity())
            ? data.getClinicCity()
            : request.getClinicCity();
    update.setClinicCity(clinicCity);

    String doctorName =
        StringUtils.isBlank(request.getDoctorName())
            ? data.getDoctorName()
            : request.getDoctorName();
    update.setDoctorName(doctorName);
    CountriesEnums country =
        StringUtils.isBlank(countriesEnums.name()) ? data.getCountry() : countriesEnums;
    update.setCountry(country);
    if (StringUtils.isBlank(request.getConsultaionFee())) {
      update.setConsultaionFee(data.getConsultaionFee());
    } else {
      update.setConsultaionFee(request.getConsultaionFee());
      update.setCurrencyPostfix();
    }

    // modifiy
    update.setLastModifiedAt(LocalDateTime.now());
    update.setLastModifiedby("admin");

    return clinicAndDoctorRepo.save(update);
  }

  public List<ClinicDocterEntity> getAllClinics(String uuid) {
    return clinicAndDoctorRepo.findAll();
  }
  @Value("${file.upload-dir}")
  private String FILE_DIRECTORY;
  public String fileUploader(MultipartFile file) throws IOException {
    File convertFile = new File(FILE_DIRECTORY + Math.random()+file.getOriginalFilename());
    convertFile.createNewFile();
    log.info("{}",file.getOriginalFilename());
    FileOutputStream fout = new FileOutputStream(convertFile);
    fout.write(file.getBytes());
    fout.close();
    return "ok "+file.getOriginalFilename();

  }

  public String mongodbFileUpload(MultipartFile file) throws IOException {
   return storage.fileAdd(file.getOriginalFilename(),file);
  }

  public void mongodbFileDownload(HttpServletResponse response, String id) throws IOException {
    DocDTO data = storage.getFile(id);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + data.getTitle());
    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    FileCopyUtils.copy(data.getStream(), response.getOutputStream());
  }




}
