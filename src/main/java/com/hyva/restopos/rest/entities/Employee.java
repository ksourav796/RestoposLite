package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String employeeAddr;
    private String employeePhone;
    private String incentives;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date employeeDOJ;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date employeeDOB;
    private String gender;
    //using for default employee in restaurant
    private String locationId;
    private String useraccount_id;
    private String accountCode;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date effectiveDate;
    private String status;
    private boolean deliveryFlag;
    private boolean waiterFlag;
}
