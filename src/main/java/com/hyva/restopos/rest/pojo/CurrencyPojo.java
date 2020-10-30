package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class CurrencyPojo {
    private Long currencyId;
    private String currencyName;
    private String currencyCode;
    private String currencySymbol;
    private String currencyDescription;
    private String status;
}

