package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.Hiposservice.HiposService;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {
    @Autowired
    CountryRepository countryRepository;

    public static Employee MapEmployeePojoToEntity(EmployeePojo employeePojo){
        Employee employee = new Employee();
        employee.setEmployeeId(employeePojo.getEmployeeId());
        employee.setEmployeeCode(employeePojo.getEmployeeCode());
        employee.setEmployeeAddr(employeePojo.getEmployeeAddr());
        employee.setEmployeeName(employeePojo.getEmployeeName());
        employee.setEmployeePhone(employeePojo.getEmployeePhone());
        employee.setIncentives(employeePojo.getIncentives());
        employee.setEmployeeDOJ(employeePojo.getEmployeeDOJ());
        employee.setEmployeeDOB(employeePojo.getEmployeeDOB());
        employee.setGender(employeePojo.getGender());
        employee.setStatus(employeePojo.getStatus());
        employee.setLocationId(employeePojo.getLocationId());
        employee.setAccountCode(employeePojo.getAccountCode());
        employee.setEffectiveDate(employeePojo.getEffectiveDate());
        employee.setStatus(employeePojo.getStatus());
        employee.setDeliveryFlag(employeePojo.isDeliveryFlag());
        employee.setWaiterFlag(employeePojo.isWaiterFlag());
        return employee;
    }

    public static List<EmployeePojo> mapEmpEntityToPojo(List<Employee> List) {
        List<EmployeePojo> list = new ArrayList<>();
        for (Employee config : List) {
            EmployeePojo employeePojo = new EmployeePojo();
            employeePojo.setEmployeeId(config.getEmployeeId());
            employeePojo.setEmployeeCode(config.getEmployeeCode());
            employeePojo.setEmployeeAddr(config.getEmployeeAddr());
            employeePojo.setEmployeeName(config.getEmployeeName());
            employeePojo.setEmployeePhone(config.getEmployeePhone());
            employeePojo.setIncentives(config.getIncentives());
            employeePojo.setEmployeeDOJ(config.getEmployeeDOJ());
            employeePojo.setEmployeeDOB(config.getEmployeeDOB());
            employeePojo.setGender(config.getGender());
            employeePojo.setLocationId(config.getLocationId());
            employeePojo.setAccountCode(config.getAccountCode());
            employeePojo.setEffectiveDate(config.getEffectiveDate());
            employeePojo.setStatus(config.getStatus());
            employeePojo.setDeliveryFlag(config.isDeliveryFlag());
            employeePojo.setWaiterFlag(config.isWaiterFlag());
            list.add(employeePojo);
        }
        return list;
    }


    public static State MapStateEntityToPojo(StatePojo statePojo){
        State state = new State();
        state.setStateId(statePojo.getStateId());
        state.setCountryId(statePojo.getCountryId());
        state.setStateName(statePojo.getStateName());
        state.setStatus(statePojo.getStatus());
        return state;
    }

    public static List<StatePojo> mapEntityToPojo(List<State> List) {
        List<StatePojo> list = new ArrayList<>();
        for (State state : List) {
            StatePojo statePojo = new StatePojo();
            statePojo.setStateId(state.getStateId());
            statePojo.setCountryId(state.getCountryId());
            statePojo.setStateName(state.getStateName());
            statePojo.setStatus(state.getStatus());
            list.add(statePojo);
        }
        return list;
    }

    public static List<AddNewItemDTO> mapItemEntityToPojo(List<Item> List) {
        List<AddNewItemDTO> list = new ArrayList<>();
        for (Item config : List) {
            AddNewItemDTO itemPojo = new AddNewItemDTO();
            itemPojo.setItemId(config.getItemId());
            itemPojo.setItemCode(config.getItemCode());
            itemPojo.setItemName(config.getItemName());
            itemPojo.setItemDesc(config.getItemDesc());
            itemPojo.setItemStatus(config.getItemStatus());
            itemPojo.setItemType(config.getItemtype());
            itemPojo.setHsnCode(config.getHsnCode());
            itemPojo.setFoodtype(config.getFoodtype());
            itemPojo.setReOrderLevel(config.getReorderlevel());
            itemPojo.setItemImage(config.getImageFile());
            itemPojo.setInclusiveJSON(config.getInclusiveJSON());
            itemPojo.setProductionItem(config.getProductionName());
            itemPojo.setSalesPrice(config.getItemPrice());
            itemPojo.setPurchasePrice(config.getItemPrice());
            itemPojo.setItemCategory(config.getIdItemCategory());
            list.add(itemPojo);
        }
        return list;
    }


}
