package training.doctor.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import training.doctor.management.model.entity.User;
import training.doctor.management.model.request.CreateUserRequest;
import training.doctor.management.model.request.LoginRequest;
import training.doctor.management.model.response.UserCreatedResposposne;
import training.doctor.management.repo.UserRepo;
import training.doctor.management.utill.JwtUtill;
import training.doctor.management.utill.PasswordUtil;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final ModelMapper mapper;
    private final JwtUtill jwtUtill;
    private final PasswordUtil passwordUtil;

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


    public String loginUser(LoginRequest request) throws Exception {
        Optional<User> userExist = repo.findUserByUserName(request.getUserName());

        if (userExist.isPresent()){
            log.error("user already exist");
            throw new RuntimeException("User AlreadyExists ");
        }
        String pwd = passwordUtil.decryptPassword(userExist.get().getUserName());
        String  reqPwd = passwordUtil.decryptPassword(request.getPassword());
        if (pwd.equals(reqPwd)){
          return   jwtUtill.createToken(userExist.get().getId(),"EBRAHIM",300000L);
        }else {
            throw new RuntimeException("Invalid UserName or Password");
        }
    }



}
