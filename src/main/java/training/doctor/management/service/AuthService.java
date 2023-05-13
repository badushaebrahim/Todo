package training.doctor.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import training.doctor.management.model.entity.User;
import training.doctor.management.model.request.CreateUserRequest;
import training.doctor.management.model.response.UserCreatedResposposne;
import training.doctor.management.repo.UserRepo;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final ModelMapper mapper;

    public UserCreatedResposposne createUser(CreateUserRequest request){
        Optional<User> userExist = repo.findUserByUserName(request.getUserName());

        if (userExist.isPresent()){
            log.error("user already exist");
            throw new RuntimeException("User AlreadyExists ");
        }
        request.setId(null);
        log.info("created new ");
        return mapper.map(repo.save(mapper.map(request,User.class)),UserCreatedResposposne.class);
    }



}
