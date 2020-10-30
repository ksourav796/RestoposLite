package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.pojo.CurrencyPojo;

public class CurrencyMapper {

    public static Currency MapCurrencyPojoToEntity(CurrencyPojo currencyPojo){
        Currency currency = new Currency();
        currency.setCurrencyId(currencyPojo.getCurrencyId());
        currency.setCurrencyName(currencyPojo.getCurrencyName());
        currency.setCurrencyCode(currencyPojo.getCurrencyCode());
        currency.setCurrencyDescription(currencyPojo.getCurrencyDescription());
        currency.setCurrencySymbol(currencyPojo.getCurrencySymbol());
        currency.setStatus(currencyPojo.getStatus());
        return currency;
    }
}
