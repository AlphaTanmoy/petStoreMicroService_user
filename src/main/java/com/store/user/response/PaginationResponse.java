package com.store.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> implements Serializable {
    private List<T> data = new ArrayList<>();
    private String offsetToken;
    private Long recordCount;
    private List<FilterOption> filterUsed = new ArrayList<>();

}
