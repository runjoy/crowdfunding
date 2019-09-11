package com.itransition.anton.service;

import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    private static String CLIENT_ID = "681561847127-n5hnvkvs6v79gjfv0lj1tqav1t0urg6a.apps.googleusercontent.com";

    private static String SECRET = "mlYfE78j2ZQPRsXxt3w32Jef";

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileLoader fileLoader;

    /*@Value("${upload.path}")
    private String uploadPath;*/

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user) {
        User userByEmail = userRepo.findByEmail(user.getEmail());
        if (userByEmail != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setBlock(false);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRegistrationDate(new Date());

        sendMessage(user);
        userRepo.save(user);
        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to task2. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getEmail(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void lockUsers(boolean lock, List<Long> lockList) {
        for(Long id : lockList) {
            User user = userRepo.findById(id).get();
            user.setBlock(lock);
            userRepo.save(user);
            if(lock) {
                //ВЫХОД ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ
                //AccountStatusUserDetailsChecker accountStatusUserDetailsChecker = new AccountStatusUserDetailsChecker();
                //accountStatusUserDetailsChecker.check(user);
            }
        }
    }

    public void deleteUsers(List<Long> deleteList) {
        for(Long id : deleteList) {
            User user = userRepo.findById(id).get();
            userRepo.delete(user);
            //ВЫХОД ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ
            //AccountStatusUserDetailsChecker accountStatusUserDetailsChecker = new AccountStatusUserDetailsChecker();
            //accountStatusUserDetailsChecker.check(user);
        }
    }

    public void adminUsers(boolean isAdmin, List<Long> adminList) {
        for(Long id : adminList) {
            User user = userRepo.findById(id).get();
            if(isAdmin) {
                user.getRoles().add(Role.valueOf("ADMIN"));
            } else {
                user.getRoles().remove(Role.valueOf("ADMIN"));

            }
            userRepo.save(user);
        }
    }

    public void editProfile(Long id, String firstname, String lastname, String city) {
        User user = userRepo.findById(id).get();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setCity(city);
        userRepo.save(user);
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).get();
    }

    public boolean changePassword(Long id, String password, String repeatpassword) {
        User user = userRepo.findById(id).get();
        if (password.equals(repeatpassword)) {
            System.out.println(password);
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public void loadImage(Long id, MultipartFile file) {
        User user = userRepo.findById(id).get();

        String uuidFile = UUID.randomUUID().toString();
        String receiverName = uuidFile + "." + file.getOriginalFilename();

        user.setFilename(receiverName);
        try {
        System.out.println("MULTIPATH: " + file.getName() + " ------- " + file.getOriginalFilename() + " -------- " + file.getContentType() + " ----" + file.getResource().getFilename());

            fileLoader.sendFileByProfile(file.getInputStream().toString(), receiverName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepo.save(user);
    }

    public boolean getShowRole(User currentUser, User editUser) {
        if (currentUser.getRoles().contains(Role.ADMIN)) {
            return true;
        } else {
            if (currentUser.getId() == editUser.getId()) {
                return true;
            }
        }
        return false;
    }
}
