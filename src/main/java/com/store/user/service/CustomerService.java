package com.store.user.service;

import com.store.user.collection.FetchMostRecentInterface;
import com.store.user.collection.GetUsers;
import com.store.user.enums.DATA_STATUS;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.repo.CustomerRepository;
import com.store.user.response.FilterOption;
import com.store.user.response.PaginationResponse;
import com.store.user.utils.ConverterStringToObjectList;
import com.store.user.utils.DateUtil;
import com.store.user.utils.EncodingUtil;
import com.store.user.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private DateUtil dateUtil;
    private CustomerRepository customerRepository;

    @Autowired
    public void CustomerRepository(DateUtil dateUtil, CustomerRepository customerRepository, EncryptionUtils encryptionUtils) throws BadRequestException {
        this.dateUtil = dateUtil;
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findCustomerById(String id) throws BadRequestException {
        return customerRepository.findById(id);
    }

    public Customer findCustomerByEmail(String email) throws BadRequestException {
        Customer findCustomer = customerRepository.findByEmail(email);
        BadRequestException badRequestException = new BadRequestException();
        badRequestException.setErrorMessage("Customer with " + email + " does not exist");
        if (findCustomer == null) throw badRequestException;
        else return findCustomer;
    }

    public void saveCustomer(Customer Customer) {
        customerRepository.save(Customer);
    }

    public PaginationResponse<GetUsers> getAllCustomers(
            String offsetToken,
            boolean considerMaxDateRange,
            List<FilterOption> toRetFilterOption,
            String dateRangeType,
            boolean giveCount,
            boolean giveData,
            int limit,
            ZonedDateTime fromDateFinal,
            ZonedDateTime toDateFinal,
            boolean showInActive,
            String userName
    ) throws Exception {
        ZonedDateTime offsetDateFinal = null;
        String offsetId = "";

        DATA_STATUS dataStatus = DATA_STATUS.ACTIVE;
        if (showInActive) dataStatus = DATA_STATUS.INACTIVE;

        if (offsetToken != null) {
            String decrypted = EncryptionUtils.decrypt(EncodingUtil.decode(offsetToken));
            String[] splits = decrypted.split("::");
            Optional<ZonedDateTime> decryptedOffsetDate =
                    dateUtil.getLocalDateTimeFromStringUsingIsoFormatServerTimeZone(splits[0]);

            if (decryptedOffsetDate.isEmpty()) {
                throw new BadRequestException("Please provide a valid offset token");
            }

            offsetDateFinal = decryptedOffsetDate.get();
            offsetId = splits[1];
        } else {
            Optional<FetchMostRecentInterface> firstCreated = customerRepository.findTop1ByOrderByCreatedDateDesc();
            if (firstCreated.isEmpty()) {
                return new PaginationResponse<>(new ArrayList<>(), null, 0L, toRetFilterOption);
            }
            FetchMostRecentInterface instant = firstCreated.get();
            offsetDateFinal = ZonedDateTime.ofInstant(instant.getCreatedDate().plusNanos(1000), ZoneId.of("UTC"));
        }

        List<GetUsers> toReturnAllUsers = new ArrayList<>();
        long giveCountData = 0;

        boolean considerFromDate = (fromDateFinal != null);
        boolean considerToDate = (toDateFinal != null);

        if (giveCount) {
            giveCountData = customerRepository.findCountWithOutOffsetIdAndDate(
                    fromDateFinal,
                    considerFromDate,
                    toDateFinal,
                    considerToDate,
                    dataStatus.name()
            );
        }

        if (giveData) {
            if (considerMaxDateRange && "MAX".equals(dateRangeType)) {
                List<GetUsers> allUsers = customerRepository.findDataWithOutOffsetIdAndDate(
                        fromDateFinal,
                        considerFromDate,
                        toDateFinal,
                        considerToDate,
                        dataStatus.name(),
                        userName
                );
                toReturnAllUsers.addAll(allUsers);
            } else {
                if (offsetId.isEmpty()) {
                    List<GetUsers> userFirstPage = customerRepository.findDataWithOutOffsetId(
                            fromDateFinal,
                            considerFromDate,
                            toDateFinal,
                            considerToDate,
                            dataStatus.name(),
                            userName,
                            limit,
                            offsetDateFinal
                    );

                    if (userFirstPage.isEmpty()) {
                        return new PaginationResponse<>(new ArrayList<>(), null, 0L, toRetFilterOption);
                    }

                    toReturnAllUsers.addAll(userFirstPage);
                } else {
                    List<GetUsers> usersNextPageWithSameData = customerRepository.findDataWithOffsetId(
                            fromDateFinal,
                            considerFromDate,
                            toDateFinal,
                            considerToDate,
                            dataStatus.name(),
                            userName,
                            limit,
                            offsetDateFinal,
                            offsetId
                    );

                    int nextPageSize = limit - usersNextPageWithSameData.size();

                    List<GetUsers> userNextPage = customerRepository.findDataWithOutOffsetId(
                            fromDateFinal,
                            considerFromDate,
                            toDateFinal,
                            considerToDate,
                            dataStatus.name(),
                            userName,
                            nextPageSize,
                            offsetDateFinal
                    );

                    toReturnAllUsers.addAll(usersNextPageWithSameData);
                    toReturnAllUsers.addAll(userNextPage);
                }
            }

            if (toReturnAllUsers.isEmpty()) {
                return new PaginationResponse<>(new ArrayList<>(), null, 0L, toRetFilterOption);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
                String formattedDate = formatter.format(toReturnAllUsers.get(toReturnAllUsers.size() - 1).getCreatedDate());

                String offsetTokenEncoded = EncodingUtil.encode(
                        EncryptionUtils.encrypt(
                                formattedDate + "::" + toReturnAllUsers.get(toReturnAllUsers.size() - 1).getId()
                        )
                );

                return new PaginationResponse<>(
                        ConverterStringToObjectList.sanitizeForOutput(toReturnAllUsers, GetUsers.class),
                        offsetTokenEncoded,
                        giveCountData,
                        toRetFilterOption
                );
            }
        } else {
            return new PaginationResponse<>(new ArrayList<>(), null, 0L, toRetFilterOption);
        }
    }

}
