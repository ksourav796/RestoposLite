/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetailDTO extends AbstractResponse implements Serializable {

    private List<SelectedItem> selectedItemsList = new ArrayList<>();
    private String operation;
    private CashPayment cashPayment;
    private List<TaxSummary> taxWiseSummaryList = new ArrayList<>();
    private CreditCardPayment creditPayment;
    private VoucherPayment voucherPayment;
    private MultiPayment multiPayment;
    private BankPayment bankPayment;
    private float hiPosServiceCharge;
    private String paymentType;
    private String tableNo;
    private double totalCheckOutamt;
    private double otherChargesAmt;
    private double totalTaxAmt;
    private double cessTotalTaxAmt;
    private int itemCount;
    private long customerId;
    private String taxType;
    private long siid;
    private String customerEmail;
    private double totalServiceCharge;
    private String siNo;
    private double billWiseDiscount;
    private String orderNo;
    private String srlnNo;
    private String orderType;
    private String cutomerName;
    private double balanceAmount;
    private double remainingAmountBalance;
    private CustomerPojo customer;
    private double discountAmount;
    private double totalTenderedAmount;
    private String userName;
    private  String printType;
    private String billToAddress;
    private String shipToAddress;
    private String mobileNo;
    private String email;
    private String phoneNumber;
    private String inventoryAddress;
    private String inventoryName;
    private String inventoryContactNo;
    private String inventoryEmail;
    private String inventoryFax;
    private String advancepayment;
    private String formNo;
    private String totalCashPymtAmtReturned;
    private String totalChequePymtAmtReturned;
    private String totalVoucherPymtAmtReturned;
    private String chequeNumber;
    private String voucherNumber;
    private String salesOrderNo;
    private String salesDeliveryOrderNO;
    private double totalActualWeight;
    private double totalSellableWeight;
    private String from_reg;
    private String to_reg;
    private String user_id;
    private String  type_doc;
    private String  type_flag;
    private String recieptFooter;
    private String companyLogoPath;
    private String cmpyLocation;
    private String custLocation;
    private int termsId;
    private String dateOfInvoice;
    private String agentIdOfInvoice;
    private String projectIdOfInvoice;
    private String termCondIdOfInvoice;
    private String currencyIdOfInvoice;
    private String exchangeRateIdOfInvoice;
    private double totalCashPayment;
    private double totalVoucherPayment;
    private double totalCardPayment;
    private String customerAddress;
    private String customerState;
    private String customerGst;
    private String locationAddress;
    private String locationState;
    private String discountType;
    private String locationGst;
    private String locationContactPerson;
    private String locationGContactNo;
    private double roundingAdj;
    private double totalAmt;
    private BigDecimal ARBalance;
    private String salesOrderId;
    private String salesOrderDetailsId;
    private String memo;
    private String invoiceType;
    private String returnReason;
    private String totalRemaininBalance;
    private Date shippingDate;
    private String shippingDateString;
    private String shippingReferenceNo;
    private String referenceNo;
    private String shippingmethodId;
    private String salesQuotationId;
    private String proFormaId;
    private String proFormaDetailsId;
    private double totalVoucherAmt;
    private double totalCreditCardAmt;
    private double totalCashAmt;
    private String patientId;
    private String serviceCharge;
    private String agentName;
    private String exchangerateValue;
    private String tc_value;
    private String salesQuotationDetailsId;
    private String otherContactId;
    private Date invoiceDate;
    private double invoiceAmt;
    private double incrementAmt;
    private String employeeId;
    private double tokenNo;
    private String table;
    private String mainTable;
    private String invoiceNo;
    private Date formDate;
    private String projectName;
    private String shippingMethodName;
    private Long invokeOrderId;
    private String invokeOrderName;
    private String generatedVoucherNo;
    private String customerPo;
    private String serviceDeliveryId;
    private String exportInvoice;
    private double invokeRemaningQty;
    private double amtToBePaid;
    private String siStatus;
    private boolean checkForHoldStock;
   private String employeeName;
   private double toStateCode;
   private double estimateCashAmount;
   private String estimateCardNo;
   private double fromStateCode;
   private String vehicleNo;
   private String distance;
   private String transporterId;
   private String customerPincode;
   private String vehicleSeries;
   private String ewayBillNo;
   public boolean roundingStatus;
   public boolean smsType;
   public String roundingOffValue;
   public String otherContactName;
   public String personIncharge;
   public String locationEmail;
   public String returnType;
   private float discountAmtInPercentage;
   private boolean bluetoothStatus;
   private String DoNo;
   private double hiposServiceChargeAmt;
   private double restaurantDiscount;
   private double amountReturned;
   private String customerNo;
    public Map<String,String> tableName;
    private String custPaymentNo;
    private Long custPaymentId;
    private Long posPaymentId;
    private String pax;
    private String discountCode;
    private String discType;
    private AirPayPayment airPayments;
    private List<InvoicePayment> invoicePaymentList = new ArrayList<>();
    private String olInvPayId;
    private boolean isAirPayPayment;

    public boolean isAirPayPayment() {
        return isAirPayPayment;
    }

    public void setAirPayPayment(boolean airPayPayment) {
        isAirPayPayment = airPayPayment;
    }
}
