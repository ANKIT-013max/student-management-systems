package com.example.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/** Small version of a department, embedded inside student / course responses. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSummaryDto{

    private Long id;
    private String name;
    private String code;
}