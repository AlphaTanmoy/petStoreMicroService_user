package com.store.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTBlackListRequest {

    @NotNull(message = "Please provide a comment")
    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 5, max = 255, message = "Comment must be between 5 and 255 characters")
    private String comment;

    @NotNull(message = "Please provide Action Taken For ID")
    @NotBlank(message = "Action Taken For ID cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Action Taken For ID must be alphanumeric (can include hyphens)")
    private String actionTakenForId;
}

