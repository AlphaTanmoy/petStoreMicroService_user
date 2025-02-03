package com.store.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAddressRequest {

    @NotNull(message = "Please provide Home/Flat Number")
    @NotBlank(message = "Home/Flat Number cannot be empty")
    @Size(min = 1, max = 50, message = "Home/Flat Number must be between 1 and 50 characters")
    private String homeFlatNumber;

    @NotNull(message = "Please provide Locality")
    @NotBlank(message = "Locality cannot be empty")
    @Size(min = 2, max = 100, message = "Locality must be between 2 and 100 characters")
    private String locality;

    @NotNull(message = "Please provide Address")
    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    @NotNull(message = "Please provide City")
    @NotBlank(message = "City cannot be empty")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotNull(message = "Please provide State")
    @NotBlank(message = "State cannot be empty")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    @NotNull(message = "Please provide Pin Code")
    @NotBlank(message = "Pin Code cannot be empty")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pin Code must be a valid 6-digit number")
    private String pinCode;

    @NotNull(message = "Please provide Mobile Number")
    @NotBlank(message = "Mobile Number cannot be empty")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile Number must be a valid 10-digit number starting with 6-9")
    private String mobile;
}

