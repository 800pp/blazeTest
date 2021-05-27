package com.blaze.cofran.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResult {
    private int totalPages;
    private List<?> page;
}
