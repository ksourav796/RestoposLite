package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long customerId;
    private String customerName;
    private String customerNumber;
    private String customerCode;
    private String companyNumber;
    //using for default customer in restaurant
    private String companyCode;
    @Column(columnDefinition="text")
    private String address;
    //   @ManyToOne
//    private Country country;
    private String attention;
    private String email;
    private String phoneNumber1;
    private String phoneNumber2;
    private String faxTelex;
    private String contactPerson;
    private String generalNote;
    private String website;
    private String gstCode;
//    @OneToOne
//    private TermsAndCondition terms;
    private boolean creditedLimitAlert;
    private String creditedLimit;
    private String creditedTerm;
    private Long country;
    private Long stateId;
    private Long currencyId;
    private String periodOfVerification;
    private String pincode;

    private String ArAccount;
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    private Date verificationDate;
    private String currencyWord;
    private String currencySymbol;
    private String shipToAddress;
    private String locationId;
    private String useraccount_id;
    private String hiConnectStatus;
    private String hiConnectCompnyRegNo;
    private String panNo;
    private String bankName;
    private String accountNo;
    private String iFSCCode;
    private String branchName;
    private String personIncharge;
    private double loyaltyPoints;
    private String status;
    private String uin;

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getFaxTelex() {
        return faxTelex;
    }

    public void setFaxTelex(String faxTelex) {
        this.faxTelex = faxTelex;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGstCode() {
        return gstCode;
    }

    public void setGstCode(String gstCode) {
        this.gstCode = gstCode;
    }

    public boolean isCreditedLimitAlert() {
        return creditedLimitAlert;
    }

    public void setCreditedLimitAlert(boolean creditedLimitAlert) {
        this.creditedLimitAlert = creditedLimitAlert;
    }

    public String getCreditedLimit() {
        return creditedLimit;
    }

    public void setCreditedLimit(String creditedLimit) {
        this.creditedLimit = creditedLimit;
    }

    public String getCreditedTerm() {
        return creditedTerm;
    }

    public void setCreditedTerm(String creditedTerm) {
        this.creditedTerm = creditedTerm;
    }


    public String getPeriodOfVerification() {
        return periodOfVerification;
    }

    public void setPeriodOfVerification(String periodOfVerification) {
        this.periodOfVerification = periodOfVerification;
    }

    public String getArAccount() {
        return ArAccount;
    }

    public void setArAccount(String arAccount) {
        ArAccount = arAccount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getCurrencyWord() {
        return currencyWord;
    }

    public void setCurrencyWord(String currencyWord) {
        this.currencyWord = currencyWord;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(String shipToAddress) {
        this.shipToAddress = shipToAddress;
    }


    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(String useraccount_id) {
        this.useraccount_id = useraccount_id;
    }

    public String getHiConnectStatus() {
        return hiConnectStatus;
    }

    public void setHiConnectStatus(String hiConnectStatus) {
        this.hiConnectStatus = hiConnectStatus;
    }

    public String getHiConnectCompnyRegNo() {
        return hiConnectCompnyRegNo;
    }

    public void setHiConnectCompnyRegNo(String hiConnectCompnyRegNo) {
        this.hiConnectCompnyRegNo = hiConnectCompnyRegNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getiFSCCode() {
        return iFSCCode;
    }

    public void setiFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPersonIncharge() {
        return personIncharge;
    }

    public void setPersonIncharge(String personIncharge) {
        this.personIncharge = personIncharge;
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(double loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
