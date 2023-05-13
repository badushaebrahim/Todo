package training.doctor.management.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import training.doctor.management.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User,String > {

    Optional<User> findUserByUserName(String userName);
}
