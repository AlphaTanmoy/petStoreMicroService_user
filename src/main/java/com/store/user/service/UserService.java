package com.store.user.service;

import com.store.user.config.JwtProvider;
import com.store.user.enums.TIRE_CODE;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.model.UserLogs;
import com.store.user.model.Users;
import com.store.user.repo.UserLogsRepository;
import com.store.user.repo.UserRepository;
import com.store.user.request.SignUpRequest;
import com.store.user.response.VerificationCodeGrabber;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserLogsRepository userLogsRepository;

    @Autowired
    private final VerificationCodeService verificationCodeService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtProvider jwtProvider;


    @Transactional
    public String createUser(SignUpRequest req, HttpServletRequest httpRequest) throws BadRequestException {
        long userCount = userRepository.countUserByEmail(req.getEmail());
        if(userCount>0){
            Users findConfirmedUser = userRepository.findByEmail(req.getEmail());
            boolean jwtBlackListCount = jwtService.isListedInJWTTable(findConfirmedUser.getEmail());
            if(jwtBlackListCount) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        long user = userRepository.countUserByEmail(req.getEmail());
        if (user==0) {
            Users createdUser = new Users();
            createdUser.setFullName(req.getFullName());
            createdUser.setEmail(req.getEmail());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            createdUser.setMobile(req.getMobile());
            createdUser.setTireCode(TIRE_CODE.TIRE4);
            userRepository.save(createdUser);

        } else {
            throw new BadRequestException("User already exists");
        }

        Users createdUser = userRepository.findByEmail(req.getEmail());

        String jwtToken = jwtProvider.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        String deviceId = UUID.randomUUID().toString();

        VerificationCodeGrabber verificationCodeGrabber = verificationCodeService.getVerificationEntityResponse(req.getEmail());

        if(verificationCodeGrabber.getOtp().isEmpty()){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("No Entity found with email>> "+req.getEmail());
        }

        verificationCodeService.saveVerificationCode(verificationCodeGrabber);

        UserLogs userDevice = new UserLogs();
        userDevice.setUser(createdUser);
        userDevice.setIpAddress(ipAddress);
        userDevice.setDeviceId(deviceId);
        userDevice.setJwtToken(jwtToken);
        userDevice.setDeviceType(httpRequest.getHeader("User-Agent"));
        userDevice.setOperatingSystem("Unknown");

        userLogsRepository.save(userDevice);

        return jwtToken;
    }
}
