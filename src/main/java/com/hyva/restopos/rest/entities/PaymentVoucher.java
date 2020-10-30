package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long vouId;
    private String vocherCode;
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Temporal(TemporalType.DATE)
    private Date toDate;
    private String status;
    private String defaultVoucher;
    private String discountType;
    private String discountAmount;
    private String minBill;
    private String maxDiscount;
    private String noOfTimesValid;

}
