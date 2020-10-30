package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class ShiftPojo {
    private Long shiftId;
    private String shiftName;
    private String fromTime;
    private String toTime;
    private String status;
    private String locationId;
}
