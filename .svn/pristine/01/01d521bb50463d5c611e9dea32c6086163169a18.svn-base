package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Company;
import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.entities.State;
import com.hyva.restopos.rest.pojo.CompanyDto;
import com.hyva.restopos.rest.pojo.CountryPojo;
import com.hyva.restopos.rest.pojo.StatePojo;

import java.util.ArrayList;
import java.util.List;

public class StateMapper {

    public static Country MapPojoToEntity(CountryPojo countryPojo){
        Country country = new Country();
        country.setCountryId(countryPojo.getCountryId());
        country.setCountryName(countryPojo.getCountryName());
        country.setStatus(countryPojo.getStatus());
        return country;
    }

    public static List<StatePojo> mapStateEntityToPojo(List<State> List) {
        List<StatePojo> list = new ArrayList<>();
        for (State State : List) {
            StatePojo statePojo = new StatePojo();
            statePojo.setStateId(State.getStateId());
            statePojo.setCountryId(State.getCountryId());
            statePojo.setStatus(State.getStatus());
            statePojo.setStateName(State.getStateName());
            list.add(statePojo);
        }
        return list;
    }



}
