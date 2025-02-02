package com.store.user.service;

import com.store.user.config.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.enums.INFO_LOG_TYPE;
import com.store.user.enums.TIRE_CODE;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.model.CustomerInfoLogger;
import com.store.user.model.CustomerLogs;
import com.store.user.model.VerificationCode;
import com.store.user.repo.*;
import com.store.user.request.LoginRequest;
import com.store.user.request.SignUpRequest;
import com.store.user.response.AuthResponse;
import com.store.user.utils.OtpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository CustomerRepository;
    private final CustomerLogsRepository CustomerLogsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImplementation customUserDetails;
    private final CustomerInfoLoggerRepository CustomerInfoLoggerRepository;
    private final JWTBlackListRepositoryCustomer JWTBlackListRepositoryCustomer;

    public void sentLoginOtp(String email) throws BadRequestException {

        long userCount = CustomerRepository.countUserByEmail(email);
        if(userCount>0){
            Customer findConfirmedUser = CustomerRepository.findByEmail(email);
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        if (email.startsWith(KeywordsAndConstants.SIGNING_PREFIX)) {
            email = email.substring(KeywordsAndConstants.SIGNING_PREFIX.length());
            Customer user=CustomerRepository.findByEmail(email);
            if(user==null) throw new BadRequestException("User not found with this email!");
        }

        List<VerificationCode> isExist = verificationCodeRepository
                .findByEmail(email);

        if (!isExist.isEmpty()) {
            verificationCodeRepository.delete(isExist.get(0));
        }

        String otp = OtpUtils.generateOTP();
        String message = "";
        if(userCount==0) message = "OTP Generated for Sign Up Request";
        else message = "OTP Generated for Login Request";

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = KeywordsAndConstants.OTP_SUBJECT_FOR_LOGIN;
        String text = KeywordsAndConstants.OTP_TEXT_FOR_LOGIN;
        CustomerInfoLogger infoLogger = new CustomerInfoLogger();
        infoLogger.setType(INFO_LOG_TYPE.OTP);

        infoLogger.setMessage(message+" ");
        CustomerInfoLoggerRepository.save(infoLogger);
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Transactional
    public String createUser(SignUpRequest req, HttpServletRequest httpRequest) throws BadRequestException {
        long userCount = CustomerRepository.countUserByEmail(req.getEmail());
        if(userCount>0){
            Customer findConfirmedUser = CustomerRepository.findByEmail(req.getEmail());
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        List<VerificationCode> verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCode == null || !verificationCode.get(0).getOtp().equals(req.getOtp())) {
            throw new BadRequestException("Wrong Otp");
        }

        long user = CustomerRepository.countUserByEmail(req.getEmail());
        if (user==0) {
            Customer createdUser = new Customer();
            createdUser.setFullName(req.getFullName());
            createdUser.setEmail(req.getEmail());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            createdUser.setMobile("9800098000");
            createdUser.setTireCode(TIRE_CODE.TIRE4);
            CustomerRepository.save(createdUser);
        } else {
            throw new BadRequestException("User already exists");
        }

        Customer createdUser = CustomerRepository.findByEmail(req.getEmail());

        String jwtToken = jwtProvider.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        String deviceId = UUID.randomUUID().toString();

        verificationCode.get(0).setCustomer(createdUser);
        verificationCodeRepository.save(verificationCode.get(0));

        CustomerLogs userDevice = new CustomerLogs();
        userDevice.setCustomer(createdUser);
        userDevice.setIpAddress(ipAddress);
        userDevice.setDeviceId(deviceId);
        userDevice.setJwtToken(jwtToken);
        userDevice.setDeviceType(httpRequest.getHeader("User-Agent"));
        userDevice.setOperatingSystem("Unknown");

        CustomerLogsRepository.save(userDevice);

        return jwtToken;
    }



    public AuthResponse signIn(LoginRequest req, HttpServletRequest httpRequest) throws BadRequestException {

        long userCount = CustomerRepository.countUserByEmail(req.getEmail());
        if(userCount>0){
            Customer findConfirmedUser = CustomerRepository.findByEmail(req.getEmail());
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        String email = req.getEmail();
        String otp = req.getOtp();

        Customer foundUser = CustomerRepository.findByEmail(req.getEmail());
        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        Authentication authentication = authenticate(email, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(foundUser.getId(), foundUser.getEmail(), foundUser.getRole());
        AuthResponse authResponse = new AuthResponse();

        String deviceId = UUID.randomUUID().toString();
        CustomerLogs userDevice = new CustomerLogs();
        userDevice.setCustomer(foundUser);
        userDevice.setIpAddress(ipAddress);
        userDevice.setDeviceId(deviceId);
        userDevice.setJwtToken(token);
        userDevice.setDeviceType(httpRequest.getHeader("User-Agent"));
        userDevice.setOperatingSystem("Unknown");
        CustomerLogsRepository.save(userDevice);

        System.out.println(email + " ----- " + otp);

        int findTotalLoggedInDevices = CustomerLogsRepository.findCountByEmail(foundUser.getEmail());

        if(findTotalLoggedInDevices > KeywordsAndConstants.MAXIMUM_DEVICE_CAN_CONNECT){
            CustomerLogsRepository.deleteAllByEmailAndLastCreated(foundUser.getEmail());
            authResponse.setMessage("Login Success But Logged Out From Most Last Logged In Device");
        }else{
            authResponse.setMessage("Login Success");
        }

        authResponse.setJwt(token);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private UsernamePasswordAuthenticationToken authenticate(String email, String otp) throws BadRequestException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null ");
            throw new BadCredentialsException("Invalid username or password");
        }
        List<VerificationCode> verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode == null || !verificationCode.get(0).getOtp().equals(otp)) {
            throw new BadRequestException("wrong otp...");
        }
        if (LocalDateTime.now().isAfter(verificationCode.get(0).getExpiryDate())) {
            verificationCodeRepository.delete(verificationCode.get(0));
            throw new BadRequestException("OTP expired...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
