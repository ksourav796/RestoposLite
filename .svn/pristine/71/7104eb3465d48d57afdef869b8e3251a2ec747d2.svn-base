package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Customer;
import com.hyva.restopos.rest.pojo.CustomerPojo;

import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public static List<CustomerPojo> mapcustomerEntityToPojo(List<Customer> List) {
        List<CustomerPojo> list = new ArrayList<>();
        for (Customer customer : List) {
            CustomerPojo customerPojo = new CustomerPojo();
            customerPojo.setCustomerId(customer.getCustomerId());
            customerPojo.setCustomerName(customer.getCustomerName());
            customerPojo.setGstCode(customer.getGstCode());
            customerPojo.setStateId(customer.getStateId());
            customerPojo.setEmail(customer.getEmail());
            customerPojo.setCurrencyId(customer.getCurrencyId());
            customerPojo.setCustomerNumber(customer.getPhoneNumber1());
            customerPojo.setAddress(customer.getAddress());
            customerPojo.setPincode(customer.getPincode());
            customerPojo.setPersonIncharge(customer.getPersonIncharge());
            customerPojo.setCountry(customer.getCountry());
            customerPojo.setStatus(customer.getStatus());
            customerPojo.setBankName(customer.getBankName());
            customerPojo.setAccountNo(customer.getAccountNo());
            customerPojo.setPanNo(customer.getPanNo());
            customerPojo.setBranchName(customer.getBranchName());
            customerPojo.setiFSCCode(customer.getiFSCCode());
            customerPojo.setWebsite(customer.getWebsite());
            customerPojo.setCreditedTerm(customer.getCreditedTerm());
            customerPojo.setCreditedLimit(customer.getCreditedLimit());
            customerPojo.setUin(customer.getUin());
            list.add(customerPojo);
        }
        return list;
    }


    public static Customer MapCustomerPojoToEntity(CustomerPojo customerPojo){
        Customer customer = new Customer();
        customer.setCustomerId(customerPojo.getCustomerId());
        customer.setCustomerName(customerPojo.getCustomerName());
        customer.setPhoneNumber1(customerPojo.getCustomerNumber());
        customer.setAddress(customerPojo.getAddress());
        customer.setEmail(customerPojo.getEmail());
        customer.setFaxTelex(customerPojo.getFaxTelex());
        customer.setContactPerson(customerPojo.getContactPerson());
        customer.setWebsite(customerPojo.getWebsite());
        customer.setGstCode(customerPojo.getGstCode());
        customer.setCreditedLimit(customerPojo.getCreditedLimit());
        customer.setPanNo(customerPojo.getPanNo());
        customer.setBankName(customerPojo.getBankName());
        customer.setAccountNo(customerPojo.getAccountNo());
        customer.setiFSCCode(customerPojo.getiFSCCode());
        customer.setBranchName(customerPojo.getBranchName());
        customer.setPersonIncharge(customerPojo.getPersonIncharge());
        customer.setStatus(customerPojo.getStatus());
        customer.setCountry(customerPojo.getCountry());
        customer.setStateId(customerPojo.getStateId());
        customer.setCurrencyId(customerPojo.getCurrencyId());
        customer.setCreditedTerm(customerPojo.getCreditedTerm());
        customer.setPincode(customerPojo.getPincode());
        customer.setUin(customerPojo.getUin());
        return customer;
    }
}
