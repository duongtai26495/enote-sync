package com.kai.mynote.service.Impl;

import com.kai.mynote.service.MailService;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.config.MyUserDetails;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserRegisterDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.*;
import com.kai.mynote.repository.*;
import com.kai.mynote.service.UserService;
import com.kai.mynote.util.JwtUtil;
import com.kai.mynote.util.UserUtil;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.kai.mynote.util.AppConstants.TIME_PATTERN;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private ActiveCodeRepository codeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private MailService mailService;

    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) throws MessagingException {
        User user = new User();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles.stream().filter(role -> role.getRole_name().equals(AppConstants.ROLE_PREFIX+AppConstants.ROLE_USER_NAME)).toList());
        user.setF_name(userRegisterDTO.getF_name());
        user.setL_name(userRegisterDTO.getL_name());
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword()));
        user.setJoined_at(dateFormat.format(date));
        user.setUpdated_at(dateFormat.format(date));
        user.setGender(userRegisterDTO.getGender());


        User createdUser = userRepository.save(user);
        sendActiveMail(user);
        return createdUser.convertDTO(createdUser);
    }

    @Override
    public UserDTO updateUser(UserUpdateDTO updateDTO) {
        User existingUser = userRepository.findFirstByUsername(updateDTO.getUsername());
        if (existingUser == null) {
            return null;
        }
        Gender gender = updateDTO.getGender();
        String firstName = updateDTO.getF_name();
        String lastName = updateDTO.getL_name();
        String profileImage = updateDTO.getProfile_image();

        if (firstName != null && !firstName.equals(existingUser.getF_name())) {
            existingUser.setF_name(firstName);
        }
        if (lastName != null && !lastName.equals(existingUser.getL_name())) {
            existingUser.setL_name(lastName);
        }
        if (gender != null && !gender.equals(existingUser.getGender())){
            existingUser.setGender(gender);
        }
        if(profileImage != null && !profileImage.equals(existingUser.getProfile_image())){
            existingUser.setProfile_image(profileImage);
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        existingUser.setUpdated_at(dateFormat.format(date));
        userRepository.save(existingUser);

        return existingUser.convertDTO(existingUser);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return new User().convertDTO(userRepository.findFirstByUsername(username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public boolean isExistByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

    @Override
    public boolean isExistByUsername(String username) {
        return userRepository.findFirstByUsername(username) != null;
    }


    @Override
    public Page<WorkSpace> getAllWorkspace(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return workspaceRepository.getAllWorkspace(username, pageable);
    }

    @Override
    public User getUserForAuthor(String username) {
        return userRepository.findFirstByUsername(username);
    }

    @Override
    public void addTokenToBlacklist(String username, String token) {
        Blacklist blacklist = new Blacklist();
        blacklist.setToken(token);
        blacklist.setUser(userRepository.findFirstByUsername(username));
        blacklistRepository.save(blacklist);
    }

    @Override
    public Blacklist checkTokenInBlacklist(String username, String token) {
        return blacklistRepository.isExistInBlacklist(getUserByUsername(username).getId(), token);
    }

    @Override
    public UserDTO updatePassword(UserUpdateDTO updateDTO) {
        User existingUser = userRepository.findFirstByUsername(updateDTO.getUsername());
        if (existingUser == null) {
            return null;
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        existingUser.setUpdated_at(dateFormat.format(date));
        String password = updateDTO.getPassword();

        if (password != null) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        userRepository.save(existingUser);

        return existingUser.convertDTO(existingUser);
    }

    @Override
    public HashMap<String, String> userAnalytics(String username) {

        HashMap<String, String> result = new HashMap<>();
        List<Task> tasks = taskRepository.getAllTasksByUsername(username);
        List<Note> notes = noteRepository.getAllNoteByUsername(username);
        long tasksCheck = tasks.stream().filter(item -> item.getType() == Type.CHECK).count();
        long isDoneTask = tasks.stream().filter(item -> item.getType() == Type.CHECK && item.isDone()).count();
        result.put("workspaces", String.valueOf(workspaceRepository.getAllWorkspaceByUsername(username).size()));
        result.put("notes", String.valueOf(notes.size()));
        result.put("tasks", String.valueOf(tasks.size()));
        result.put("tasksDone", String.valueOf(isDoneTask));

        result.put("percentageTasks", String.valueOf(percentageCalc(isDoneTask, tasksCheck)));

        long isDoneNote = notes.stream().filter(item -> item.getProgress() == 100.0).count();

        result.put("percentageNotes", String.valueOf(isDoneNote));

        return result;
    }

    @Override
    public void setActiveUser(String email, boolean activate) {
        User user = userRepository.findFirstByEmail(email);
        user.setEnabled(activate);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        user.setUpdated_at(dateFormat.format(date));
        userRepository.save(user);
    }

    @Override
    public void sendActiveMail(User user) {
        Date currentDate = new Date();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(currentDate);

        Date current = currentTime.getTime();

        Calendar expiredTime = Calendar.getInstance();
        expiredTime.add(Calendar.MINUTE, 5);
        Date expired = expiredTime.getTime();

        ActiveCode activeCode = new ActiveCode();
        activeCode.setUsername(user.getUsername());
        activeCode.setEmail(user.getEmail());
        activeCode.setCreatedAt(current);
        activeCode.setExpiredAt(expired);


        activeCode.setCode(userUtil.generateRandomString().toLowerCase());
        codeRepository.save(activeCode);
        String content_active_mail = String.format(AppConstants.ACTIVE_EMAIL_CONTENT,activeCode.getCode());
        try {
            mailService.sendHtmlEmail(user.getEmail(),AppConstants.SUBJECT_CONTENT,content_active_mail);
            logger.info("Mail sent to: "+user.getEmail());
        }catch (MessagingException e) {
            logger.error("Mail sending error: "+e);
        }
    }

    private double percentageCalc(long smallNum, long bigNum){
        if(smallNum > 0 && smallNum < bigNum){
            return ((double) smallNum / bigNum) * 100;
        }
        return 0.0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new MyUserDetails(user);
    }
}
