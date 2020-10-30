/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectedItem {
    public String itemName;
    private long purchaseInvoiceDetailID;
    private long itemId;
    private double unitPrice;
    private double unitPriceIn;
    private double convertedUnitPrice;
    private double qty;
    private double returnQty;
    private double remainingQty;
    private double amtexclusivetax;
    private long  taxid;
    private long  inputTaxId;
    private double taxpercent;
    private double taxamt;
    private double amtinclusivetax;
    private double discountAmt;
    private String itemDescription;
    private String taxName;
    public String serializableItemId;
    public String serializableNumbers;
    public String itemCode;
    private double makingCharge;
    private double actualWeight;
    private double cess;
    private String message;
    private double cgstsgstsplitvalues;
    private double taxPercentageSplit;
    private double cessTaxAmt;
    private String hsnCode;
    private String salesOrderId;
    private String salesOrderDetailsId;
    private double posamtexclusivetax;
    private double posamtinclusivetax;
    private boolean receiveItemFlag;
    private boolean flag;
    private String receiveItemId;
    private String receiveItemFormNo;
    private  String taxCode;
    private Long ProFormaSalesInvoiceId;
    private Long ProFormaSalesInvoiceDetailsId;
    private Long salesQuotationId;
    private Long salesQuotationDetailsId;
    private String inclusiveJSON;
    private Long purchaseQuotationId;
    private Long purchaseOrderId;
    private Long salesOrder;
    private String batchNo;
    private String  batchExpDate;
    private String serializableIMEINo;
//    private List<BatchItemDto> batchNoList;
//    private List<UomConvertorDTO> uomConvertorList;
//    private List<ItemSerializeDTO> serializeNoList;
//    private List<SerializableItemsDTO> serializableItemsDTOs;
//    List<SerializableItemsDTO> serializableItemsDTOList;
    private String uom;
    private String uomName;
    private Long uomConvertorId;
    private String  uomValue;
    private double convertedReturnQty;
    private Long uomId;
    private Long sIItemId;
    private String convertedName;
    private Long assertId;
//    private UomConvertor uomConvertor;
    private String customerName;
    private Long id;
    private double deliverdQuantity;
    private String siid;
    private Date batchDate;
    private double convertedQuantity;
    private String itemType;
    private double fifoAmount;
    private String salesDeliverOrderId;
    private String salesDeliverOrderDetailsId;
    private double received;
    private double qtytotalSent;
    private double discPercent;
    private double itemAmount;
    private String delStatus;
    private String uomConvertedName;
    private Long receiveItemDetailsId;
    private Long customerId;
    private String tablesId;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;
    private String a6;
    private String a7;
    private String a8;
    private String a9;
    private String a10;
    public String locationId;
    public String useraccount_id;
    private Date expireDate;
    private Date purchaseDate;
    private Date manufactureDate;
    private String sORbNumbers;
    private String serializableImeiNo;
    private double itemCommisionAmount;
    private double itemCommisionConfigAmount;
    private String  type;
    private Double discountConfigAmt;
    private double serviceRemainingQty;
    private Date fromDate;
    private Date toDate;
    private String serializableStatus;
    private Long itemCategoryId;
    private String itemCategoryName;
    private double pORemaningQty;
    private String status;
    private Long purchaseOrderDetailsId;
    private Long salesOrderDetailId;
    private double deliveredQuantity;
    private double QtySent;
    private double actualQty;
    private String productMergerSubItemNames;
    private String otherCharges;
    private String piNo;
    private long salesInvoiceDetailsId;
    private String serviceDescription;
    private String invoiceNo;
    private int uomConvertedId;
    private String assestName;

   }
