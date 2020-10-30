package com.hyva.restopos.rest.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethodDTO {
    private Long paymentmethodId;
    private String paymentmethodName;
    private String paymentmethodDescription;
    private String paymentmethodType;
    private String companyId;
    private String accountMaster;
    private String status;
   private String defaultType;
   private Long accountMasterId;
   private String validateVoucher;
    private String merchantId;
    private String uniqueId;
    private String locationId;
    private String useraccount_id;
    private String secretKey;

    public String getValidateVoucher() {
        return validateVoucher;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(String useraccount_id) {
        this.useraccount_id = useraccount_id;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setValidateVoucher(String validateVoucher) {
        this.validateVoucher = validateVoucher;
    }

    public Long getAccountMasterId() {
        return accountMasterId;
    }

    public void setAccountMasterId(Long accountMasterId) {
        this.accountMasterId = accountMasterId;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPaymentmethodId() {
        return paymentmethodId;
    }

    public void setPaymentmethodId(Long paymentmethodId) {
        this.paymentmethodId = paymentmethodId;
    }

    public String getPaymentmethodName() {
        return paymentmethodName;
    }

    public void setPaymentmethodName(String paymentmethodName) {
        this.paymentmethodName = paymentmethodName;
    }

    public String getPaymentmethodDescription() {
        return paymentmethodDescription;
    }

    public void setPaymentmethodDescription(String paymentmethodDescription) {
        this.paymentmethodDescription = paymentmethodDescription;
    }

    public String getPaymentmethodType() {
        return paymentmethodType;
    }

    public void setPaymentmethodType(String paymentmethodType) {
        this.paymentmethodType = paymentmethodType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccountMaster() {
        return accountMaster;
    }

    public void setAccountMaster(String accountMaster) {
        this.accountMaster = accountMaster;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
