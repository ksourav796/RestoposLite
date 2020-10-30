package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.pojo.CurrencyPojo;

import java.util.ArrayList;
import java.util.List;

public class CurrecncyMapper {

    public static Currency  mapPojoToEntity (CurrencyPojo currencyPojo){
        Currency currency = new Currency();
        currency.setCurrencyId(currencyPojo.getCurrencyId());
        currency.setCurrencyName(currencyPojo.getCurrencyName());
        currency.setCurrencyCode(currencyPojo.getCurrencyCode());
        currency.setCurrencyDescription(currencyPojo.getCurrencyDescription());
        currency.setCurrencySymbol(currencyPojo.getCurrencySymbol());
        currency.setStatus(currencyPojo.getStatus());
        return currency;
    }

    public static List<CurrencyPojo> mapEntityToPojo(List<Currency> List) {
        List<CurrencyPojo> list = new ArrayList<>();
        for (Currency config : List) {
            CurrencyPojo currencyPojo = new CurrencyPojo();
            currencyPojo.setCurrencyId(config.getCurrencyId());
            currencyPojo.setCurrencyName(config.getCurrencyName());
            currencyPojo.setCurrencyCode(config.getCurrencyCode());
            currencyPojo.setCurrencyDescription(config.getCurrencyDescription());
            currencyPojo.setCurrencySymbol(config.getCurrencySymbol());
            currencyPojo.setStatus(config.getStatus());
            list.add(currencyPojo);
        }
        return list;
    }

}
