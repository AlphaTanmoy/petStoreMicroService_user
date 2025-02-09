package com.store.user.controller;

import com.store.user.collection.GetCustomers;
import com.store.user.utils.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.response.FilterOption;
import com.store.user.response.PaginationResponse;
import com.store.user.service.CustomerService;
import com.store.user.utils.Processor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtProvider jwtProvider;
    private final CustomerService customerService;
    private final Processor processor;

    @GetMapping("/getAll")
    PaginationResponse<GetCustomers> getAllCustomers(
            @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false) String token,
            @RequestParam(value = "giveCount", required = false, defaultValue = "false") boolean giveCount,
            @RequestParam(value = "giveData", required = false, defaultValue = "true") boolean giveData,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "dateRangeType", required = false) String dateRangeType,
            @RequestParam(value = "considerMaxDateRange", required = false, defaultValue = "false") boolean considerMaxDateRange,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offsetToken", required = false) String offsetToken,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "showInActive", required = false) boolean showInActive
    ) {
        List<FilterOption> toRetFilterOption = new ArrayList<>();
        String role;
        if (token != null) {
            role = jwtProvider.getRoleFromJwtToken(token);
        } else {
            throw new BadRequestException("Customer not found");
        }

        if(Objects.equals(role, USER_ROLE.ROLE_ADMIN.toString()) || Objects.equals(role, USER_ROLE.ROLE_MASTER.name())){
            String userNameFinal = processor.processSimpleFilter(userName, "userName", toRetFilterOption);

            ZonedDateTime fromDateFinal = processor.processDateFilter(fromDate, "fromDate", toRetFilterOption);
            ZonedDateTime toDateFinal = processor.processDateFilter(toDate, "toDate", toRetFilterOption);

            if (fromDateFinal != null && toDateFinal != null && fromDateFinal.isAfter(toDateFinal)) {
                throw new BadRequestException("From Date should be before To Date.");
            }

            if (dateRangeType != null) {
                toRetFilterOption.add(new FilterOption("dateRangeType", dateRangeType, dateRangeType));
            }

            toRetFilterOption.add(new FilterOption("considerMaxDateRange", String.valueOf(considerMaxDateRange), String.valueOf(considerMaxDateRange)));

            int pageSizeFinal = KeywordsAndConstants.DEFAULT_PAGE_SIZE;
            if (limit != null) {
                pageSizeFinal = Math.min(limit, KeywordsAndConstants.DEFAULT_PAGE_SIZE);
            }
            try{
                return customerService.getAllCustomers(
                        offsetToken,
                        considerMaxDateRange,
                        toRetFilterOption,
                        dateRangeType,
                        giveCount,
                        giveData,
                        pageSizeFinal,
                        fromDateFinal,
                        toDateFinal,
                        showInActive,
                        userNameFinal
                );
            } catch (Exception e){
                throw new BadRequestException("Can't able to process right now!");
            }

        }
         else {
             throw new BadRequestException("You are not authorised to use this route!");
        }
    }

}