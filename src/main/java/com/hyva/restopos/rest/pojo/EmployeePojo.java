package com.hyva.restopos.rest.pojo;

import lombok.Data;

import javax.persistence.Temporal;
import java.util.Date;
@Data
public class EmployeePojo {
    private Long EmployeeId;
    private String EmployeeCode;
    private String EmployeeName;
    private String EmployeeAddr;
    private String EmployeePhone;
    private String incentives;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date EmployeeDOJ;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date EmployeeDOB;
    private String Gender;
    private String status;
    private String locationId;
    private String useraccount_id;
    private String AccountCode;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date effectiveDate;
    private boolean deliveryFlag;
    private boolean waiterFlag;
}
