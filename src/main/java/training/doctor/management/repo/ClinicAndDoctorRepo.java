package training.doctor.management.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import training.doctor.management.model.entity.ClinicDocterEntity;
@Repository
public interface ClinicAndDoctorRepo extends MongoRepository<ClinicDocterEntity,String > {

}
