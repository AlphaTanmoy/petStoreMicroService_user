package com.store.user.service;

import com.store.user.config.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.enums.INFO_LOG_TYPE;
import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.TIRE_CODE;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.model.*;
import com.store.user.repo.*;
import com.store.user.request.AddAddressRequest;
import com.store.user.request.LoginRequest;
import com.store.user.request.SignUpRequest;
import com.store.user.response.AuthResponse;
import com.store.user.response.GetDeviceDetails;
import com.store.user.utils.DeviceUtils;
import com.store.user.utils.GenerateUUID;
import com.store.user.utils.OtpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import static com.store.user.config.KeywordsAndConstants.AUTH_MICROSERVICE_BASE_URL_LOC;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final CustomerLogsRepository customerLogsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImplementation customUserDetails;
    private final CustomerInfoLoggerRepository customerInfoLoggerRepository;
    private final JWTBlackListRepositoryCustomer JWTBlackListRepositoryCustomer;
    private final CustomerAddressRepository customerAddressRepository;
    private final DeviceUtils deviceUtils;

    public void sentLoginOtp(String email) throws BadRequestException {

        long userCount = customerRepository.countUserByEmail(email);
        if(userCount>0){
            Customer findConfirmedUser = customerRepository.findByEmail(email);
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        if (email.startsWith(KeywordsAndConstants.SIGNING_PREFIX)) {
            email = email.substring(KeywordsAndConstants.SIGNING_PREFIX.length());
            Customer user=customerRepository.findByEmail(email);
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
        customerInfoLoggerRepository.save(infoLogger);
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Transactional
    public String createCustomer(SignUpRequest req, HttpServletRequest httpRequest, MICROSERVICE microservice) throws BadRequestException {
        Customer findConfirmedUser = customerRepository.findByEmail(req.getEmail());

        if (findConfirmedUser != null && !findConfirmedUser.getId().isEmpty()) {
            return null;
        }

        if (findConfirmedUser != null) {
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if (jwtBlackListCount > 0) {
                throw new BadRequestException(
                        findConfirmedUser.getFullName() + ", You are blackListed. Contact Support For Remove As BlackList" + findConfirmedUser.getRole() + "!"
                );
            }
        }

        List<VerificationCode> verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode.isEmpty()) {
            throw new BadRequestException("No verification code found for this email");
        }

        if (!verificationCode.get(0).getOtp().equals(req.getOtp())) {
            throw new BadRequestException("Wrong OTP");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = AUTH_MICROSERVICE_BASE_URL_LOC + "/signUp";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> authRequestBody = new HashMap<>();
            authRequestBody.put("fullName", req.getFullName());
            authRequestBody.put("email", req.getEmail());
            authRequestBody.put("otp", verificationCode.get(0).getOtp());
            authRequestBody.put("mobileNumber", req.getMobileNumber());
            authRequestBody.put("microServiceName", microservice);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);

            if (authResponse.getStatusCode() != HttpStatus.OK) {
                throw new BadRequestException("Failed to create user in Authentication Microservice");
            }

            // Create & Save Customer first, then flush to ensure it's persisted
            Customer createdUser = new Customer();
            createdUser.setFullName(req.getFullName());
            createdUser.setEmail(req.getEmail());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            createdUser.setTireCode(TIRE_CODE.TIRE4);
            createdUser = customerRepository.save(createdUser);
            customerRepository.flush(); // Ensure Hibernate persists the Customer

            // Now associate the saved Customer with VerificationCode
            verificationCode.get(0).setCustomer(createdUser);
            verificationCodeRepository.save(verificationCode.get(0));

            String jwtToken = jwtProvider.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());

            String ipAddress = httpRequest.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = httpRequest.getRemoteAddr();
            }

            GetDeviceDetails deviceDetails = deviceUtils.findDeviceDetails(httpRequest.getHeader("User-Agent"));

            CustomerLogs userLogs = new CustomerLogs();
            userLogs.setCustomer(createdUser);
            userLogs.setIpAddress(ipAddress);
            userLogs.setJwtToken(jwtToken);
            userLogs.setDeviceId(GenerateUUID.generateShortUUID());
            userLogs.setDeviceType(deviceDetails.getDeviceType());
            userLogs.setOperatingSystem(deviceDetails.getOperatingSystem());
            userLogs.setMicroserviceName(MICROSERVICE.USER);
            customerLogsRepository.save(userLogs);

            return jwtToken;
        } catch (Exception e) {
            throw new BadRequestException("Authentication Microservice Response Error");
        }
    }


    public String addAddress(AddAddressRequest addAddressRequest, HttpServletRequest httpServletRequest, Customer customer) throws BadRequestException{
        long userCount = customerRepository.countUserByEmail(customer.getEmail());
        if(userCount>0){
            Customer findConfirmedUser = customerRepository.findByEmail(customer.getEmail());
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        CustomerAddress customerAddress = getCustomerAddress(addAddressRequest, customer);
        customerAddressRepository.save(customerAddress);

        Set<CustomerAddress> userAddresses = customer.getUserAddresses();
        userAddresses.add(customerAddress);
        customer.setUserAddresses(userAddresses);

        customerRepository.save(customer);

        return jwtProvider.generateToken(customer.getId(), customer.getEmail(), customer.getRole());
    }

    public AuthResponse signIn(LoginRequest req, HttpServletRequest httpRequest) throws BadRequestException {

        long userCount = customerRepository.countUserByEmail(req.getEmail());
        if(userCount>0){
            Customer findConfirmedUser = customerRepository.findByEmail(req.getEmail());
            Long jwtBlackListCount = JWTBlackListRepositoryCustomer.findByUserId(findConfirmedUser.getId());
            if(jwtBlackListCount>0) throw new BadRequestException(
                    findConfirmedUser.getFullName()+", You are blackListed. Contact Support For Remove As BlackList"+findConfirmedUser.getRole()+"!"
            );
        }

        String email = req.getEmail();
        String otp = req.getOtp();

        Customer foundUser = customerRepository.findByEmail(req.getEmail());
        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        Authentication authentication = authenticate(email, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(foundUser.getId(), foundUser.getEmail(), foundUser.getRole());
        AuthResponse authResponse = new AuthResponse();

        String deviceId = UUID.randomUUID().toString();
        CustomerLogs customerLogs = new CustomerLogs();
        customerLogs.setCustomer(foundUser);
        customerLogs.setIpAddress(ipAddress);
        customerLogs.setDeviceId(deviceId);
        customerLogs.setJwtToken(token);
        customerLogs.setDeviceType(httpRequest.getHeader("User-Agent"));
        customerLogs.setOperatingSystem("Unknown");
        customerLogsRepository.save(customerLogs);

        System.out.println(email + " ----- " + otp);

        int findTotalLoggedInDevices = customerLogsRepository.findCountByEmail(foundUser.getEmail());

        if(findTotalLoggedInDevices > KeywordsAndConstants.MAXIMUM_DEVICE_CAN_CONNECT){
            customerLogsRepository.deleteAllByEmailAndLastCreated(foundUser.getEmail());
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

    private static CustomerAddress getCustomerAddress(AddAddressRequest addAddressRequest, Customer customer) {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setHomeFlatNumber(addAddressRequest.getHomeFlatNumber());
        customerAddress.setLocality(addAddressRequest.getLocality());
        customerAddress.setAddress(addAddressRequest.getAddress());
        customerAddress.setCity(addAddressRequest.getCity());
        customerAddress.setState(addAddressRequest.getState());
        customerAddress.setPinCode(addAddressRequest.getPinCode());
        customerAddress.setMobile(addAddressRequest.getMobile());
        customerAddress.setCustomer(customer);
        return customerAddress;
    }
}
