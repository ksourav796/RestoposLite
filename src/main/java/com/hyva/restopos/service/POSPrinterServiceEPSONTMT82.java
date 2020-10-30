package com.hyva.restopos.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hyva.restopos.rest.Hiposservice.HiposService;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.*;
import com.hyva.restopos.util.POSPrinterBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service("EPSONTMT82")
@Qualifier("EPSONTMT82")
public class POSPrinterServiceEPSONTMT82 implements BasePosPrinterService{

    @Autowired
    private HiposService hiposService;
    @Autowired
    UserAccountSetupRepository userAccountSetupRepository;
    @Autowired
    AccountSetupRepository accountSetupRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TableConfigRepository tableConfigRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Value("${printerType}")
    public String buildType;

    private Map<String,List> cloudPrinterConf;
    private List<String> printerData;

    private Logger logger = Logger.getLogger(POSPrinterServiceEPSONTMT82.class);

    @SuppressWarnings("Duplicates")
    public Map formatAndPrintBill(RetailPrintDTO retailPrintDTO) {
        UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
        AccountSetup accountSetup=accountSetupRepository.findOne(1L);
        Gson gson = new Gson();
        Type type1 = new TypeToken<PrinterSelectionDisplayPojo>(){}.getType();
        PrinterSelectionDisplayPojo printerSelectionDisplayPojo = new PrinterSelectionDisplayPojo();
        printerSelectionDisplayPojo = gson.fromJson(accountSetup.getPrintDetails(),type1);
        POSPrinterBuilder posPrinterBuilder = new POSPrinterBuilder();
        cloudPrinterConf = new HashMap<>();

        CashPayment cashPayment                 = retailPrintDTO.getSiData().getCashPayment();
        BankPayment bankPayment                 = retailPrintDTO.getSiData().getBankPayment();
        CreditCardPayment creditCardPayment     = retailPrintDTO.getSiData().getCreditPayment();
        VoucherPayment voucherPayment           = retailPrintDTO.getSiData().getVoucherPayment();

        posPrinterBuilder.resetAll();
        posPrinterBuilder.initialize();
        posPrinterBuilder.feedBack((byte) 2);

        if(StringUtils.pathEquals(printerSelectionDisplayPojo.getFont(),"2")) {
            posPrinterBuilder.chooseFont(2);
        }else {
            posPrinterBuilder.chooseFont(3);
        }
        Company company = companyRepository.findAllByStatus("Active");
        //Company Name
        posPrinterBuilder.alignCenter();
        posPrinterBuilder.setText(company.getCompanyName().toUpperCase());
        posPrinterBuilder.newLine();
        posPrinterBuilder.chooseFont(2);
        //Description
        if(!StringUtils.isEmpty(printerSelectionDisplayPojo.getCompanyDescription())){
            posPrinterBuilder.setText(printerSelectionDisplayPojo.getCompanyDescription());
            posPrinterBuilder.newLine();
        }
        //Address
        if(!StringUtils.isEmpty(company.getAddress())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getAddress(),"Yes")) {
            posPrinterBuilder.setText(company.getAddress());
            posPrinterBuilder.newLine();
        }
        //Pincode
        if(!StringUtils.isEmpty(company.getPincode())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getPincode(),"Yes")) {
            posPrinterBuilder.setText(company.getPincode());
            posPrinterBuilder.newLine();
        }
        //Phone
            if (!StringUtils.isEmpty(company.getPhone())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getPhone(),"Yes")) {
                posPrinterBuilder.setText("Phone: " + company.getPhone());
                posPrinterBuilder.newLine();
            }
        //GSTIN
            if (!StringUtils.isEmpty(company.getGstNo())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getGstn(),"Yes")) {
                posPrinterBuilder.setText("GSTIN: " + company.getGstNo());
                posPrinterBuilder.newLine();
            }
        //Email
        if(!StringUtils.isEmpty(company.getEmail())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCompanyEmail(),"Yes")){
            posPrinterBuilder.setText("Email: " + company.getEmail());
            posPrinterBuilder.newLine();
        }
        //Date
        if(StringUtils.pathEquals(printerSelectionDisplayPojo.getDate(),"Yes")) {
            posPrinterBuilder.setText("Date:" + retailPrintDTO.getRestaurantDate());
            posPrinterBuilder.newLine();
        }
        //Invoice No.
        if(StringUtils.pathEquals(printerSelectionDisplayPojo.getInvoiceNo(),"Yes")) {
            posPrinterBuilder.setText("Invoice Number:" + retailPrintDTO.getSiData().getSiNo());
            posPrinterBuilder.newLine();
        }
        if(!accountSetup.isCopytokot()) {
            if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getEmployeeName())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getSteward(),"Yes")) {
                posPrinterBuilder.setText("Steward: " + retailPrintDTO.getSiData().getEmployeeName());
                posPrinterBuilder.newLine();
            }
            if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getOrderNo())){
                if(StringUtils.pathEquals(printerSelectionDisplayPojo.getAgent(),"Yes")) {
                    posPrinterBuilder.setText("Aggregator: " + retailPrintDTO.getSiData().getAgentName());
                    posPrinterBuilder.newLine();
                }
                if(StringUtils.pathEquals(printerSelectionDisplayPojo.getOrderId(),"Yes")) {
                    posPrinterBuilder.setText("Order No: " + retailPrintDTO.getSiData().getOrderNo());
                    posPrinterBuilder.newLine();
                }
            }else {
                if(StringUtils.pathEquals(printerSelectionDisplayPojo.getTable(),"Yes")) {
                    posPrinterBuilder.setText("Table No: " + retailPrintDTO.getSiData().getMainTable());
                    posPrinterBuilder.newLine();
                }
            }
        }else {
            if(StringUtils.pathEquals(printerSelectionDisplayPojo.getTable(),"Yes")) {
                posPrinterBuilder.setText("Table No: " + "Walkin");
                posPrinterBuilder.newLine();
            }
        }
        if(StringUtils.isEmpty(retailPrintDTO.getSiData().getOrderNo())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getTokenNo(),"Yes")) {
            posPrinterBuilder.setText("Kot No: " + retailPrintDTO.getSiData().getEmail());
            posPrinterBuilder.newLine();
        }
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getPax())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getPax(),"Yes")){
            posPrinterBuilder.setText("Pax: " + retailPrintDTO.getSiData().getPax());
            posPrinterBuilder.newLine();
        }
        if (StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerName(), "Yes")) {
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("    Customer Details:   ");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("Customer Name: " + retailPrintDTO.getSiData().getCutomerName());
            posPrinterBuilder.newLine();
        }
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerNo())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerPhone(),"Yes")) {
            posPrinterBuilder.setText("Phone: " + retailPrintDTO.getSiData().getCustomerNo());
            posPrinterBuilder.newLine();
        }
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerAddress())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerAddress(),"Yes")) {
            posPrinterBuilder.setText("Address: "+retailPrintDTO.getSiData().getCustomerAddress());
            posPrinterBuilder.newLine();
        }
//        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerPincode())&&StringUtils.pathEquals(printerSelectionDisplayPojo.getPincode(),"Yes")) {
//            posPrinterBuilder.setText("Pincode: "+retailPrintDTO.getSiData().getCustomerPincode());
//            posPrinterBuilder.newLine();
//        }

        posPrinterBuilder.chooseFont(2);

        posPrinterBuilder.alignCenter();
        //3-Inch Space Set
        posPrinterBuilder.setText("------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText(" Name                    Price     Qty    Amount");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("------------------------------------------------\n");

        double sumAmtExclusiveTax = 0;
        //- means left justified
        for (SelectedItem item : retailPrintDTO.getSiData().getSelectedItemsList()) {
            posPrinterBuilder.setText(""
                    + String.format("%-25s", String.valueOf(item.getItemName()).substring(0,Math.min(item.getItemName().length(),24)).toUpperCase()) + ""
                    + String.format("%-9s", String.format("%.02f", item.getUnitPrice())) + ""
                    + String.format("%-4s", String.format("%.02f", item.getQty())) + ""
                    + String.format("%9s", String.format("%.02f", item.getUnitPrice()*item.getQty())) + ""
                    + "\n");
            posPrinterBuilder.alignLeft();
            if(item.getItemName().length()>24){
                posPrinterBuilder.setText(""
                        + String.format("%-25s", String.valueOf(item.getItemName()).substring(25,Math.min(item.getItemName().length(),48)).toUpperCase()) + ""
                        + "\n");
            }
            posPrinterBuilder.alignCenter();
            sumAmtExclusiveTax +=item.getUnitPrice()*item.getQty();
        }
        double discountValue=0;
        discountValue = retailPrintDTO.getSiData().getDiscountAmount();
        discountValue *= 1;
        posPrinterBuilder.setText("------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText(" Sub Total :                      " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax)) + "\n");
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getDiscountType())) {
            if (!StringUtils.pathEquals(retailPrintDTO.getSiData().getDiscountType(), "itemWise")) {
                if (discountValue > 0 && !StringUtils.isEmpty(retailPrintDTO.getSiData().getDiscType())&& StringUtils.pathEquals(retailPrintDTO.getSiData().getDiscType(),"Percentage")) {
                    posPrinterBuilder.setText(" Discount (" + String.format("%.02f", retailPrintDTO.getSiData().getDiscountAmtInPercentage()) + "%) :              " + String.format("%14s", String.format("%.02f", discountValue) + "\n"));
                }else {
                    if (discountValue > 0) {
                        posPrinterBuilder.setText(" Discount :                        " + String.format("%14s", String.format("%.02f", discountValue) + "\n"));
                    }
                }
            }else {
                if (discountValue > 0) {
                    posPrinterBuilder.setText(" Discount :                        " + String.format("%14s", String.format("%.02f", discountValue) + "\n"));
                }
            }
        }

        posPrinterBuilder.setText("------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText(" Net Total :                      " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax-discountValue)) + "\n");
//        if(retailPrintDTO.getSiData().getHiposServiceChargeAmt() !=0){
//            int i=0;
//            HiposServiceChargeDTO hiPosServiceCharge=hiposDAO.getServiceChargeListByNameOrCode(null,null,null,null,userAccountSetup).get(0);
//            i=15-hiPosServiceCharge.getServiceChargeName().length();
//            if(i==0||i<0){
//                i=1;
//            }else {
//                i=i+2;
//            }
//            posPrinterBuilder.setText(" "+hiPosServiceCharge.getServiceChargeName().substring(0,Math.min(hiPosServiceCharge.getServiceChargeName().length(),15))+"("+String.format("%.02f",retailPrintDTO.getSiData().getHiPosServiceCharge())+"%) :           "+String.format("%"+i+"s","")+String.format("%11s", String.format("%.02f",retailPrintDTO.getSiData().getHiposServiceChargeAmt())+"\n"));
//        }
        for(TaxSummary taxSummary:retailPrintDTO.getSiData().getTaxWiseSummaryList()){
            posPrinterBuilder.setText(" CGST ("+String.format("%.02f",taxSummary.getTaxPercent()/2)+"%) :                    "+String.format("%14s", String.format("%.02f",taxSummary.getTaxAmount() / 2)+"\n"));
            posPrinterBuilder.setText(" SGST ("+String.format("%.02f",taxSummary.getTaxPercent()/2)+"%) :                    "+String.format("%14s", String.format("%.02f",taxSummary.getTaxAmount() / 2)+"\n"));
        }

        if(retailPrintDTO.getSiData().getRoundingOffValue() != null) {
            double roundOffValue = Double.parseDouble(retailPrintDTO.getSiData().getRoundingOffValue());
            if(roundOffValue!=0) {
                if (roundOffValue >=0.5)
                    roundOffValue = Math.abs(roundOffValue);
                else
                    roundOffValue = -1*roundOffValue;
                posPrinterBuilder.setText(" Rounding Off :                   " + String.format("%14s", String.format("%.02f", roundOffValue)) + "\n");
            }
        }
        posPrinterBuilder.setText("------------------------------------------------\n");
        posPrinterBuilder.chooseFont(11);
        posPrinterBuilder.setText(" Grand Total:                        " + String.format("%11s", String.format("%.02f", retailPrintDTO.getSiData().getTotalCheckOutamt())) + "\n");
        posPrinterBuilder.chooseFont(2);
        posPrinterBuilder.setText("------------------------------------------------\n");
        posPrinterBuilder.chooseFont(2);
        if(retailPrintDTO.getSiData().getTotalCheckOutamt()>0) {
            posPrinterBuilder.setText("Payment Type:\n");
            posPrinterBuilder.setText("---------------\n");
        }
        //Max length of String '57'
        if(cashPayment!=null && cashPayment.getTotalCPAmountTendered() > 0){
            posPrinterBuilder.setText("Cash:"+String.format("%44s", String.format("%.02f", cashPayment.getTotalCPAmountTendered())+"\n"));
        }

        if(bankPayment!=null && bankPayment.getTotalBPAmountTendered() > 0){
            List<MultiBankPayment> multiBankPaymentLst = bankPayment.getMultiBankPaymentList();
            for(MultiBankPayment multiBankPayment:multiBankPaymentLst){
                posPrinterBuilder.setText(multiBankPayment.getPaymentName()+":"+String.format("%44s", String.format("%.02f", multiBankPayment.getAmount())+"\n"));
//                if(!StringUtils.isEmpty(multiBankPayment.getBankName()))
//                    posPrinterBuilder.setText("Bank Name : "+multiBankPayment.getBankName()+"\n");
//                if(!StringUtils.isEmpty(multiBankPayment.getTransactionNo()))
//                    posPrinterBuilder.setText("Cheque No : "+multiBankPayment.getTransactionNo()+"\n");
//                try {
//                    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//                    String dateFormat = df.format(multiBankPayment.getDate());
//                    posPrinterBuilder.setText("Date : "+dateFormat+"\n");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                posPrinterBuilder.setText("Amount : "+multiBankPayment.getAmount()+"\n");
            }
        }
        if(creditCardPayment!=null && creditCardPayment.getTotalCCPAmountTendered() > 0){
            posPrinterBuilder.newLine();
            List<MultiCardPayment> multiCardPaymentLst = creditCardPayment.getCardPaymentList();
            for(MultiCardPayment multiCardPayment:multiCardPaymentLst){
                posPrinterBuilder.setText(multiCardPayment.getPaymentName()+":"+String.format("%44s", String.format("%.02f", multiCardPayment.getCardAmt())+"\n"));
//                if(!StringUtils.isEmpty(multiCardPayment.getCardNo()))
//                    posPrinterBuilder.setText("Card No : "+multiCardPayment.getCardNo()+"\n");
//                posPrinterBuilder.setText("Card Amt : "+multiCardPayment.getCardAmt()+"\n");
            }
        }
        if(voucherPayment!=null && voucherPayment.getTotalVPAmountTendered() > 0){
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("Voucher Discount: "+String.format("%31s", String.format("%.02f",voucherPayment.getTotalVPAmountTendered())+"\n"));
//            for(MultiVoucherPayment multiVoucherPayment:voucherPayment.getMultiVoucherPayments()){
//                if (!StringUtils.isEmpty(multiVoucherPayment.getVoucherNo())) {
//                    posPrinterBuilder.setText("Voucher No : " + multiVoucherPayment.getVoucherNo() + "\n");
//                }
//                posPrinterBuilder.setText("Voucher Amt : " + multiVoucherPayment.getVoucherAmt() + "\n");
//
//            }
        }
        posPrinterBuilder.newLine();
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getMemo())){
            posPrinterBuilder.setText("Memo: " + retailPrintDTO.getSiData().getMemo());
            posPrinterBuilder.newLine();
        }
        posPrinterBuilder.alignCenter();
        if(!StringUtils.isEmpty(printerSelectionDisplayPojo.getFooterText())) {
            posPrinterBuilder.setText(printerSelectionDisplayPojo.getFooterText());
        }
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Designed & Powered By RestoPos");

        posPrinterBuilder.feed((byte) 3);
        posPrinterBuilder.finit();

        AccountSetupPojo accountSetupDTO = hiposService.getConfigureDetails();
        TablesPos tablesPos=null;
        TableConfig tableConfig=null;

        List<PosPrinterDto> posPrinterDtos = new ArrayList<>();
        Type type = new TypeToken<List<PosPrinterDto>>() {
        }.getType();
        String printersName = null;
        if (!StringUtils.isEmpty(accountSetup.getReportPrinter())) {
            posPrinterDtos = gson.fromJson(accountSetup.getReportPrinter(), type);
            if (!StringUtils.isEmpty(retailPrintDTO.getSiData().getTableNo())) {
                tablesPos = hiposService.getTablePosObj(retailPrintDTO.getSiData().getTableNo());
                tableConfig = tableConfigRepository.findAllByConfigurationname(tablesPos.getConfigurationname());
                for (PosPrinterDto posPrinterDto : posPrinterDtos) {
                    if(tableConfig!=null&&userAccountSetup!=null)
                        if(StringUtils.pathEquals(posPrinterDto.getTableConfig(),tableConfig.getTableconfigid().toString())&& StringUtils.pathEquals(posPrinterDto.getUser(),userAccountSetup.getFullName())){
                            printersName = posPrinterDto.getPosPrinter();
                        }
                }
            }
            if (StringUtils.isEmpty(printersName)) {
                printersName = posPrinterDtos.get(0).getPosPrinter();
            }
        }
        if(buildType.equals("cloud"))
        {
            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), accountSetupDTO.getReportPrinter());
            {
                if(StringUtils.pathEquals(retailPrintDTO.getPrintType(),"mobile")){
                    String printerName = accountSetupDTO.getMobileBillPrinter();
                    byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                    printerData = cloudPrinterConf.get(printerName);
                    if(printerData == null) {
                        printerData = new ArrayList();
                        cloudPrinterConf.put(printerName,printerData);
                    }
                    try {
                        printerData.add(new String(data, "UTF-8"));
                    }catch(UnsupportedEncodingException e){e.printStackTrace();}
                }else {
                    byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                    printerData = cloudPrinterConf.get(printersName);
                    if(printerData == null) {
                        printerData = new ArrayList();
                        cloudPrinterConf.put(printersName,printerData);
                    }
                    try {
                        printerData.add(new String(data, "UTF-8"));
                    }catch(UnsupportedEncodingException e){e.printStackTrace();}
                }
            }
        }
        else{
            if(!StringUtils.isEmpty(retailPrintDTO.getPrintType())&&StringUtils.pathEquals(retailPrintDTO.getPrintType(),"mobile")) {
                feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), accountSetupDTO.getMobileBillPrinter());
            }else {
                feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), printersName);
            }
        }

        return cloudPrinterConf;
    }
    public Map formatAndPrintBillSI(RetailPrintDTO retailPrintDTO) {
        POSPrinterBuilder posPrinterBuilder = new POSPrinterBuilder();
        cloudPrinterConf = new HashMap<>();

        CashPayment cashPayment                 = retailPrintDTO.getSiData().getCashPayment();
        BankPayment bankPayment                 = retailPrintDTO.getSiData().getBankPayment();
        CreditCardPayment creditCardPayment     = retailPrintDTO.getSiData().getCreditPayment();
        VoucherPayment voucherPayment           = retailPrintDTO.getSiData().getVoucherPayment();

        posPrinterBuilder.resetAll();
        posPrinterBuilder.initialize();
        posPrinterBuilder.feedBack((byte) 2);

        posPrinterBuilder.chooseFont(2);
        Company company = companyRepository.findAllByStatus("Active");
        //Company Name
        posPrinterBuilder.alignCenter();
        posPrinterBuilder.setText(company.getCompanyName());
        posPrinterBuilder.newLine();
        UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
        //Address
        if(!StringUtils.isEmpty(company.getAddress())) {
            posPrinterBuilder.setText(company.getAddress());
            posPrinterBuilder.newLine();
        }
        //Phone
        if(!StringUtils.isEmpty(company.getPhone())) {
            posPrinterBuilder.setText("Phone: " + company.getPhone());
            posPrinterBuilder.newLine();
        }
        //GSTIN
        if(!StringUtils.isEmpty(company.getGstNo())) {
            posPrinterBuilder.setText("GSTIN: " + company.getGstNo());
            posPrinterBuilder.newLine();
        }
        //Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedDate=null;
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            formattedDate = simpleDateFormat.parse(retailPrintDTO.getDate());
            String dateFormat = df.format(formattedDate);
            posPrinterBuilder.setText("Date:" + dateFormat);
            posPrinterBuilder.newLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Invoice No.
        posPrinterBuilder.setText("Invoice Number:" + retailPrintDTO.getSiData().getSiNo());
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Customer Name: " + retailPrintDTO.getSiData().getCutomerName());
        posPrinterBuilder.newLine();
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerNo())) {
            posPrinterBuilder.setText("Phone: " + retailPrintDTO.getSiData().getCustomerNo());
            posPrinterBuilder.newLine();
        }
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerAddress())) {
            posPrinterBuilder.setText(retailPrintDTO.getSiData().getCustomerAddress());
            posPrinterBuilder.newLine();
        }
        posPrinterBuilder.newLine();

        posPrinterBuilder.chooseFont(1);

        posPrinterBuilder.alignCenter();
        //3-Inch Space Set
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Name                      Price           Qty            Amount");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("---------------------------------------------------------------\n");

        double sumAmtExclusiveTax = 0;
        //- means left justified
        for (SelectedItem item : retailPrintDTO.getSiData().getSelectedItemsList()) {
            posPrinterBuilder.setText(""
                    + String.format("%-24s", String.valueOf(item.getItemName()).substring(0,Math.min(item.getItemName().length(),23)).toUpperCase()) + ""
                    + String.format("%8s", String.format("%.02f", item.getUnitPrice())) + ""
                    + String.format("%10s", String.format("%.02f", item.getQty())) + ""
                    + String.format("%20s", String.format("%.02f", item.getUnitPrice()*item.getQty())) + ""
                    + "\n");
            sumAmtExclusiveTax +=item.getUnitPrice()*item.getQty();
        }
        double discountValue=0;
        discountValue =retailPrintDTO.getSiData().getDiscountAmount();
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText(" Sub Total :                                     " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax)) + "\n");

        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getDiscountType())) {
            if (!StringUtils.pathEquals(retailPrintDTO.getSiData().getDiscountType(), "itemWise")) {
                if (discountValue > 0 && retailPrintDTO.getSiData().getDiscountAmtInPercentage() > 0) {
                    posPrinterBuilder.setText(" Discount (" + String.format("%.02f", retailPrintDTO.getSiData().getDiscountAmtInPercentage()) + "%) :                       " + String.format("%14s", String.format("%.02f", discountValue) + "\n"));
                }
            }else {
                if (discountValue > 0) {
                    posPrinterBuilder.setText(" Discount :                                    " + String.format("%14s", String.format("%.02f", discountValue) + "\n"));
                }
            }
        }
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText(" Net Total :                                     " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax-discountValue)) + "\n");
        if(retailPrintDTO.getSiData().getTotalTaxAmt()>0) {
            posPrinterBuilder.setText(" GST Amount :                                     " + String.format("%14s", String.format("%.02f", retailPrintDTO.getSiData().getTotalTaxAmt()) + "\n"));
        }
        posPrinterBuilder.newLine();
        if(retailPrintDTO.getSiData().getRoundingOffValue() != null) {
            double roundOffValue = Double.parseDouble(retailPrintDTO.getSiData().getRoundingOffValue());
            if(roundOffValue!=0) {
                if (roundOffValue >=0.5)
                    roundOffValue = Math.abs(roundOffValue);
                else
                    roundOffValue = -1*roundOffValue;
                posPrinterBuilder.setText("Rounding Off :                        " + String.format("%14s", String.format("%.02f", roundOffValue)) + "\n");
            }
        }
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.chooseFont(11);
        posPrinterBuilder.setText(" Grand Total:                        " + String.format("%11s", String.format("%.02f", retailPrintDTO.getSiData().getTotalCheckOutamt())) + "\n");
        posPrinterBuilder.newLine();
        posPrinterBuilder.chooseFont(1);
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.chooseFont(1);
        posPrinterBuilder.setText("Payment Type:\n");
        posPrinterBuilder.setText("---------------\n");



        //Max length of String '57'
        if(cashPayment!=null && cashPayment.getTotalCPAmountTendered() > 0){
            posPrinterBuilder.setText("Cash:"+String.format("%60s", String.format("%.02f", cashPayment.getTotalCPAmountTendered())+"\n\n"));
        }

        if(bankPayment!=null && bankPayment.getMultiBankPaymentList().size() > 0){
            posPrinterBuilder.setText(bankPayment.getPaymentType()+":"+String.format("%58s", String.format("%.02f", bankPayment.getTotalBPAmountTendered())+"\n"));
            List<MultiBankPayment> multiBankPaymentLst = bankPayment.getMultiBankPaymentList();
            for(MultiBankPayment multiBankPayment:multiBankPaymentLst){
                if(!StringUtils.isEmpty(multiBankPayment.getBankName()))
                    posPrinterBuilder.setText("Bank Name : "+multiBankPayment.getBankName()+"\n");
                if(!StringUtils.isEmpty(multiBankPayment.getTransactionNo()))
                    posPrinterBuilder.setText("Cheque No : "+multiBankPayment.getTransactionNo()+"\n");
                try {
                    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    String dateFormat = df.format(multiBankPayment.getDate());
                    posPrinterBuilder.setText("Date      : "+dateFormat+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                posPrinterBuilder.setText("Amount    : "+multiBankPayment.getAmount()+"\n");
            }
        }
        if(creditCardPayment!=null && creditCardPayment.getCardPaymentList().size() > 0){
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText(creditCardPayment.getPaymentType()+":"+String.format("%58s", String.format("%.02f", creditCardPayment.getTotalCCPAmountTendered())+"\n"));
            List<MultiCardPayment> multiCardPaymentLst = creditCardPayment.getCardPaymentList();
            for(MultiCardPayment multiCardPayment:multiCardPaymentLst){
                if(!StringUtils.isEmpty(multiCardPayment.getCardNo()))
                    posPrinterBuilder.setText("Card No   : "+multiCardPayment.getCardNo()+"\n");
                posPrinterBuilder.setText("Card Amt   : "+multiCardPayment.getCardAmt()+"\n");
            }
        }
        if(voucherPayment!=null && voucherPayment.getMultiVoucherPayments().size() > 0){
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("Voucher Discount: "+String.format("%45s", String.format("%.02f",voucherPayment.getTotalVPAmountTendered())+"\n"));
            for(MultiVoucherPayment multiVoucherPayment:voucherPayment.getMultiVoucherPayments()){
                if (!StringUtils.isEmpty(multiVoucherPayment.getVoucherNo())) {
                    posPrinterBuilder.setText("Voucher No   : " + multiVoucherPayment.getVoucherNo() + "\n");
                }
                posPrinterBuilder.setText("Voucher Amt   : " + multiVoucherPayment.getVoucherAmt() + "\n");

            }
        }
        posPrinterBuilder.newLine();
        if( retailPrintDTO.getSiData().getTotalTenderedAmount()-retailPrintDTO.getSiData().getTotalCheckOutamt()>0)
            posPrinterBuilder.setText("Amount Returned:  "+String.format("%50s", String.format("%.02f", retailPrintDTO.getSiData().getTotalTenderedAmount()-retailPrintDTO.getSiData().getTotalCheckOutamt())+"\n\n"));
        if( retailPrintDTO.getSiData().getTotalCheckOutamt()-retailPrintDTO.getSiData().getTotalTenderedAmount()>0)
            posPrinterBuilder.setText("Balance Amount:  "+String.format("%50s", String.format("%.02f", retailPrintDTO.getSiData().getTotalCheckOutamt()-retailPrintDTO.getSiData().getTotalTenderedAmount())+"\n\n"));
        posPrinterBuilder.setText("---------------------------------------------------------------\n");
        posPrinterBuilder.setText("GST         Taxable Amt        CGST         SGST         Gst Amt");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("---------------------------------------------------------------\n");

        //- means left justified
        double taxPer=0;
//        for(TaxSummary taxSummary:retailPrintDTO.getSiData().getTaxWiseSummaryList()){
//            posPrinterBuilder.setText(""
//                    + String.format("%-24s",String.format("%.02f",taxSummary.getTaxPercent()/2)+"%" + ""
//                    + String.format("%13s", String.format("%.02f", taxSummary.getTaxableAmt())) )+ ""
//                    + String.format("%10s",String.format("%.02f",taxSummary.getTaxPercent()/2))+""
//                    + String.format("%10s",String.format("%.02f",taxSummary.getTaxPercent()/2))+""
//                    + String.format("%15s", String.format("%.02f", taxSummary.getTaxAmount())) + ""
//                    + "\n");
//            taxPer=taxPer+taxSummary.getTaxPercent();
//
//        }
        posPrinterBuilder.setText("---------------------------------------------------------------\n");
        posPrinterBuilder.setText(""
                + String.format("%-24s","Total:" + ""
                + String.format("%13s", String.format("%.02f",sumAmtExclusiveTax)) )+ ""
                + String.format("%10s",String.format("%.02f",taxPer/2))+""
                + String.format("%10s",String.format("%.02f",taxPer/2))+""
                + String.format("%15s", String.format("%.02f", retailPrintDTO.getSiData().getTotalTaxAmt())) + ""
                + "\n");
        posPrinterBuilder.newLine();
        posPrinterBuilder.alignCenter();
        posPrinterBuilder.setText("Thank You Visit Again..");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Designed & Powered By RestoPos");

        posPrinterBuilder.feed((byte) 3);
        posPrinterBuilder.finit();

        AccountSetupPojo accountSetupPojo = hiposService.getConfigureDetails();
        TablesPos tablesPos=null;
        TableConfig tableConfig=null;
        Gson gson = new Gson();
        List<PosPrinterDto> posPrinterDtos = new ArrayList<>();
        Type type = new TypeToken<List<PosPrinterDto>>() {
        }.getType();
        String printerName = null;
        if (!StringUtils.isEmpty(accountSetupPojo.getReportPrinter())) {
            posPrinterDtos = gson.fromJson(accountSetupPojo.getReportPrinter(), type);
            if (!StringUtils.isEmpty(retailPrintDTO.getSiData().getTableNo())) {
                tablesPos = hiposService.getTablePosObj(retailPrintDTO.getSiData().getTableNo());
                tableConfig = hiposService.tableConfigurationByTableName(tablesPos.getConfigurationname());
                for (PosPrinterDto posPrinterDto : posPrinterDtos) {
                    if(tableConfig!=null&&userAccountSetup!=null)
                        if(StringUtils.pathEquals(posPrinterDto.getTableConfig(),tableConfig.getTableconfigid().toString())&& StringUtils.pathEquals(posPrinterDto.getUser(),userAccountSetup.getFullName())){
                            printerName = posPrinterDto.getPosPrinter();
                        }
                }
            }
            if (StringUtils.isEmpty(printerName)) {
                printerName = posPrinterDtos.get(0).getPosPrinter();
            }
        }
        if(buildType.equals("cloud"))
        {
            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), accountSetupPojo.getReportPrinter());
            {
                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                printerData = cloudPrinterConf.get(printerName);
                if(printerData == null) {
                    printerData = new ArrayList();
                    cloudPrinterConf.put(printerName,printerData);
                }
                try {
                    printerData.add(new String(data, "UTF-8"));
                }catch(UnsupportedEncodingException e){e.printStackTrace();}
            }
        }
        else{
            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), printerName);
        }
        return cloudPrinterConf;
    }
    public Map formatAndPrintBillEstimate(RetailPrintDTO retailPrintDTO,String locationId,int userId) {
        POSPrinterBuilder posPrinterBuilder = new POSPrinterBuilder();
        cloudPrinterConf = new HashMap<>();
        posPrinterBuilder.resetAll();
        posPrinterBuilder.initialize();
        posPrinterBuilder.feedBack((byte) 2);

        posPrinterBuilder.chooseFont(2);
        Company company = companyRepository.findAllByStatus("Active");
        //Company Name
        posPrinterBuilder.alignCenter();
        posPrinterBuilder.setText(company.getCompanyName());
        posPrinterBuilder.newLine();

        //Address
        UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
        //Address
        if(!StringUtils.isEmpty(company.getAddress())) {
            posPrinterBuilder.setText(company.getAddress());
            posPrinterBuilder.newLine();
        }
        //Phone
        if(!StringUtils.isEmpty(company.getPhone())) {
            posPrinterBuilder.setText("Phone: " + company.getPhone());
            posPrinterBuilder.newLine();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy @hh:mm a");
        Date formattedDate=null;
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            formattedDate = simpleDateFormat.parse(retailPrintDTO.getDate());
            String dateFormat = df.format(formattedDate);
            posPrinterBuilder.setText("Date:" + dateFormat);
            posPrinterBuilder.newLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Invoice No.
        posPrinterBuilder.setText("Estimate Number:" + retailPrintDTO.getSiData().getSiNo());
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Customer Name: " + retailPrintDTO.getSiData().getCutomerName());
        posPrinterBuilder.newLine();
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerNo())) {
            posPrinterBuilder.setText("Phone: " + retailPrintDTO.getSiData().getCustomerNo());
            posPrinterBuilder.newLine();
        }
        Customer customer=customerRepository.findByCustomerName(retailPrintDTO.getSiData().getCutomerName());
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerNo())) {
            posPrinterBuilder.setText("GSTIN: " + customer.getGstCode());
            posPrinterBuilder.newLine();
        }
        if(!StringUtils.isEmpty(retailPrintDTO.getSiData().getCustomerAddress())) {
            posPrinterBuilder.setText(retailPrintDTO.getSiData().getCustomerAddress());
            posPrinterBuilder.newLine();
        }
        posPrinterBuilder.chooseFont(1);

        posPrinterBuilder.alignCenter();
        //3-Inch Space Set
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Name                      Price           Qty            Amount");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("---------------------------------------------------------------\n");

        double sumAmtExclusiveTax = 0;
        //- means left justified
        for (SelectedItem item : retailPrintDTO.getSiData().getSelectedItemsList()) {
            posPrinterBuilder.setText(""
                    + String.format("%-24s", String.valueOf(item.getItemName()).substring(0,Math.min(item.getItemName().length(),23)).toUpperCase()) + ""
                    + String.format("%8s", String.format("%.02f", item.getUnitPrice())) + ""
                    + String.format("%10s", String.valueOf((int) item.getQty())) + ""
                    + String.format("%20s", String.format("%.02f", item.getUnitPrice()*item.getQty())) + ""
                    + "\n");
            sumAmtExclusiveTax=item.getUnitPrice()*item.getQty()+sumAmtExclusiveTax;

        }
        double discountValue=0;
        if(retailPrintDTO.getSiData().getDiscountAmtInPercentage()>0) {
            discountValue = retailPrintDTO.getSiData().getDiscountAmount();
        }
        discountValue *= 1;
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("---------------------------------------------------------------");
        posPrinterBuilder.newLine();
        posPrinterBuilder.chooseFont(11);
        posPrinterBuilder.setText(" Net Total :                      " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax-discountValue)) + "\n");
        posPrinterBuilder.chooseFont(1);
        posPrinterBuilder.newLine();
        posPrinterBuilder.alignCenter();
        posPrinterBuilder.setText("No Bargain,No Refund");
        posPrinterBuilder.newLine();
        posPrinterBuilder.setText("Thank You Visit Again..");

        posPrinterBuilder.feed((byte) 3);
        posPrinterBuilder.finit();

        AccountSetupPojo accountSetupPojo = hiposService.getConfigureDetails();
        TablesPos tablesPos=null;
        TableConfig tableConfig=null;
        Gson gson = new Gson();
        List<PosPrinterDto> posPrinterDtos = new ArrayList<>();
        Type type = new TypeToken<List<PosPrinterDto>>() {
        }.getType();
        String printerName = null;
        if (!StringUtils.isEmpty(accountSetupPojo.getReportPrinter())) {
            posPrinterDtos = gson.fromJson(accountSetupPojo.getReportPrinter(), type);
            if (!StringUtils.isEmpty(retailPrintDTO.getSiData().getTableNo())) {
                tablesPos = hiposService.getTablePosObj(retailPrintDTO.getSiData().getTableNo());
                tableConfig = tableConfigRepository.findAllByConfigurationname(tablesPos.getConfigurationname());
                for (PosPrinterDto posPrinterDto : posPrinterDtos) {
                    if(tableConfig!=null&&userAccountSetup!=null)
                        if(StringUtils.pathEquals(posPrinterDto.getTableConfig(),tableConfig.getTableconfigid().toString())&& StringUtils.pathEquals(posPrinterDto.getUser(),userAccountSetup.getFullName())){
                            printerName = posPrinterDto.getPosPrinter();
                        }
                }
            }
            if (StringUtils.isEmpty(printerName)) {
                printerName = posPrinterDtos.get(0).getPosPrinter();
            }
        }
        if(buildType.equals("cloud"))
        {
            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), accountSetupPojo.getReportPrinter());
            {
                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                printerData = cloudPrinterConf.get(printerName);
                if(printerData == null) {
                    printerData = new ArrayList();
                    cloudPrinterConf.put(printerName,printerData);
                }
                try {
                    printerData.add(new String(data, "UTF-8"));
                }catch(UnsupportedEncodingException e){e.printStackTrace();}
            }
        }
        else{
            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), printerName);
        }
        return cloudPrinterConf;
    }

    @SuppressWarnings("Duplicates")
    public Map placeOrdersToKOT(List<POSKOTItemOrderDTO> kitchenOrders, String tableName, String waiterName,String tokenNo,String orderType,String customerName,String instructions,String orderNo,String pax,String agentName) {
        //Table
        //Waiter :
        //Token Number :
        //Date: 07/05/2018 1:18PM
        Gson gson = new Gson();
        Company company = companyRepository.findAllByStatus("Active");
//        if(!StringUtils.isEmpty(location)&& !StringUtils.pathEquals(location,"1")){
//            company.setCompanyName(inv.getInventoryLocationName());
//        }



        cloudPrinterConf = new HashMap<>();
        JSONObject masterCategoryJsonObj = getParentChildConfiguration();
        //JSONObject masterCategoryJsonObj = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        List<String> categoryList = new ArrayList<>();
        POSPrinterBuilder posPrinterBuilder = new POSPrinterBuilder();
        AccountSetupPojo accountSetupPojo = hiposService.getConfigureDetails();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy @ hh:mm a");
        String strDate = formatter.format(date);
        String kotPrinterList = accountSetupPojo.getKotPrinter();
        String val = kotPrinterList.substring(1, kotPrinterList.length() - 1);
        String result = val.replaceAll("[{}]", "");
        kotPrinterList = "{" + result + "}";
        try {
            jsonObject = new JSONObject(kotPrinterList);
            Iterator<String> iterator = jsonObject.keys();
            iterator.forEachRemaining(categoryList::add);
            //remove default category
            categoryList.remove("undefined");

            //Company Name
            posPrinterBuilder.chooseFont(4);
            posPrinterBuilder.newLine();
            posPrinterBuilder.alignCenter();
            posPrinterBuilder.setText(company.getCompanyName().toUpperCase());
            posPrinterBuilder.newLine();
            //Category KOT
            for (String categoryName : categoryList) {
                posPrinterBuilder = new POSPrinterBuilder();
                posPrinterBuilder.resetAll();
                posPrinterBuilder.initialize();
                posPrinterBuilder.feedBack((byte) 2);


                if(orderType.equals("Cancel")){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText("Cancel\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("------------------------------------------------------------\n");
                }
                if(tableName.equals("TAKEAWAY")||tableName.equals("Delivery")){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("------------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText(tableName+"\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("------------------------------------------------------------\n");
                }
                if(!tableName.equals("TAKEAWAY")&&!tableName.equals("Delivery")&& StringUtils.isEmpty(agentName)){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText("DineIn\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                }
                if(!tableName.equals("TAKEAWAY")&&!tableName.equals("Delivery")&&!StringUtils.isEmpty(agentName)){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText(agentName+"\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                }
                posPrinterBuilder.chooseFont(4);
                if(!StringUtils.pathEquals(tableName,"Delivery")&&!StringUtils.pathEquals(tableName,"TAKEAWAY")) {
                    posPrinterBuilder.setText("Table  :" + tableName + "\n");
                }
                posPrinterBuilder.chooseFont(2);
//                posPrinterBuilder.setText("CustomerName  :" + customerName + "\n");
                if(!StringUtils.isEmpty(waiterName)&&(!StringUtils.pathEquals(tableName,"Delivery")&&!StringUtils.pathEquals(tableName,"TAKEAWAY"))) {
                    posPrinterBuilder.setText("Steward :" + waiterName + "\n");
                    posPrinterBuilder.setText("Punched By :" + waiterName + "\n");
                }
                posPrinterBuilder.setText("Kot No   :" + tokenNo + "\n");
                if(!StringUtils.isEmpty(orderNo)) {
                    posPrinterBuilder.setText("OrderNo :" + orderNo + "\n");
                }
                if(!StringUtils.isEmpty(pax)){
                    posPrinterBuilder.setText("Pax     :" + pax     + "\n");
                }
                posPrinterBuilder.chooseFont(4);
                posPrinterBuilder.setText("Date   :" + strDate + "\n");
                posPrinterBuilder.newLine();
                posPrinterBuilder.chooseFont(1);


                posPrinterBuilder.setText("---------------------------------------------------------------");
                posPrinterBuilder.newLine();
                posPrinterBuilder.setText("   Item Name                               Qty                 ");
                posPrinterBuilder.newLine();
                posPrinterBuilder.setText("---------------------------------------------------------------\n");

                ArrayList<String> childCategories = gson.fromJson(masterCategoryJsonObj.optString(categoryName).trim(),ArrayList.class);
                List<POSKOTItemOrderDTO> filteredList = null;

                if(childCategories == null) {
                    filteredList = kitchenOrders.stream().filter(kitchenOrder -> kitchenOrder.getItemCategoryName().equals(categoryName)).collect(Collectors.toList());
                }else{
                    filteredList = kitchenOrders.stream().filter(kitchenOrder -> childCategories.contains(kitchenOrder.getItemCategoryName().trim())).collect(Collectors.toList());
                }

                for (POSKOTItemOrderDTO order : filteredList) {
                    double qty=order.getItemQty();
                    String quantity=null;
                    if((qty-(int)qty)!=0){
                        quantity=String.valueOf(qty);
                    }else {
                        quantity=String.valueOf((double) qty);
                    }
                    String decimalPattern = "([0-9]*)\\.([1-9]*)";
                    boolean match = Pattern.matches(decimalPattern, quantity);
                    if(match==true){
                        quantity=String.valueOf((double) qty);
                        double f = Double.parseDouble(quantity);
                        quantity= String.format("%.2f", new BigDecimal(f));
                    }else {
                        quantity=String.valueOf((int) qty);
                    }
                    posPrinterBuilder.chooseFont(9);
                    if(!order.getItemName().contains("CHARGE")){
                        posPrinterBuilder.setText("  "
                                + String.format("%-30s", String.valueOf(order.getItemName().substring(0,Math.min(order.getItemName().length(),30)))) + "  "
                                + String.format("%-3s", String.valueOf(quantity)) + ""
                                + "\n");
                    }

                    posPrinterBuilder.chooseFont(6);
                    if(!StringUtils.isEmpty(order.getItemDescription())) {
                        posPrinterBuilder.setText(String.format("%-32s", order.getItemDescription().length() <= 30?"("+String.valueOf(order.getItemDescription().substring(0,Math.min(order.getItemDescription().length(),30)))+")":"("+String.valueOf(order.getItemDescription().substring(0,Math.min(order.getItemDescription().length(),30)))) + "" + "\n");
                        if (order.getItemDescription().length() > 30) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-32s", order.getItemDescription().length() <= 61?String.valueOf(order.getItemDescription()).substring(31, Math.min(order.getItemDescription().length(), 61))+")":String.valueOf(order.getItemDescription()).substring(31, Math.min(order.getItemDescription().length(), 61))) + ""
                                    + "\n");
                        }
                        if (order.getItemDescription().length() > 61) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-32s", order.getItemDescription().length() <= 92?String.valueOf(order.getItemDescription()).substring(62, Math.min(order.getItemDescription().length(), 92))+")":String.valueOf(order.getItemDescription()).substring(62, Math.min(order.getItemDescription().length(), 92))) + ""
                                    + "\n");
                        }
                        if (order.getItemDescription().length() > 92) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-32s", String.valueOf(order.getItemDescription()).substring(93, Math.min(order.getItemDescription().length(), 125)))+")" + ""
                                    + "\n");
                        }
                    }
                }
                if(!StringUtils.isEmpty(instructions)) {
                    posPrinterBuilder.newLine();
                    posPrinterBuilder.setText("Instructions: " + instructions);
                    posPrinterBuilder.newLine();
                }
                posPrinterBuilder.feed((byte) 3);
                posPrinterBuilder.finit();

                if (!filteredList.isEmpty()) {
                    kitchenOrders.removeAll(filteredList);
                    JSONObject cancelKOTPrinter = new JSONObject(accountSetupPojo.getBillModel());
                    if(orderType.equals("Order") || (orderType.equals("Cancel") && cancelKOTPrinter.optBoolean("reportToCategory"))) {
                        if(buildType.equals("cloud"))
                        {
                            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString(categoryName));
                            {
                                String printerNames = jsonObject.getString(categoryName);
                                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                                printerData = cloudPrinterConf.get(printerNames);
                                if(printerData == null) {
                                    printerData = new ArrayList();
                                    cloudPrinterConf.put(printerNames,printerData);
                                }
                                printerData.add(new String(data,"UTF-8"));
                            }
                        }
                        else {
                            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString(categoryName));
                        }
                    }
                    if(orderType.equals("Cancel") && cancelKOTPrinter.optBoolean("reportToReceipt"))
                        if(buildType.equals("cloud"))
                        {
                            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString(categoryName));
                            {
                                String printerNames = jsonObject.getString(categoryName);
                                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                                printerData = cloudPrinterConf.get(printerNames);
                                if(printerData == null) {
                                    printerData = new ArrayList();
                                    cloudPrinterConf.put(printerNames,printerData);
                                }
                                printerData.add(new String(data,"UTF-8"));
                            }
                        }
                        else {
                            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString(categoryName));
                        }
                }
            }

            //ALL Category KOT
            {
                posPrinterBuilder = new POSPrinterBuilder();
                posPrinterBuilder.resetAll();
                posPrinterBuilder.initialize();
                posPrinterBuilder.feedBack((byte) 2);
                //Company Name
                posPrinterBuilder.chooseFont(4);
                posPrinterBuilder.newLine();
                posPrinterBuilder.alignCenter();
                posPrinterBuilder.setText(company.getCompanyName().toUpperCase());
                posPrinterBuilder.newLine();
                if(orderType.equals("Cancel")){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText("Cancel\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------");
                }
                if(tableName.equals("TAKEAWAY")||tableName.equals("Delivery")){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText(tableName+"\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------");
                }
                if(!tableName.equals("TAKEAWAY")&&!tableName.equals("Delivery")&& StringUtils.isEmpty(agentName)){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText("DineIn\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                }
                if(!tableName.equals("TAKEAWAY")&&!tableName.equals("Delivery")&&!StringUtils.isEmpty(agentName)){
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                    posPrinterBuilder.alignCenter();
                    posPrinterBuilder.chooseFont(3);
                    posPrinterBuilder.setText(agentName+"\n");
                    posPrinterBuilder.chooseFont(1);
                    posPrinterBuilder.setText("--------------------------------------------------------\n");
                }
                posPrinterBuilder.newLine();
                posPrinterBuilder.chooseFont(4);
                if(!StringUtils.pathEquals(tableName,"Delivery")&&!StringUtils.pathEquals(tableName,"TAKEAWAY")) {
                    posPrinterBuilder.setText("Table  :" + tableName + "\n");
                }
                posPrinterBuilder.chooseFont(2);
//                posPrinterBuilder.setText("CustomerName  :" + customerName + "\n");
                if(!StringUtils.isEmpty(waiterName)&&(!StringUtils.pathEquals(tableName,"Delivery")&&!StringUtils.pathEquals(tableName,"TAKEAWAY"))) {
                    posPrinterBuilder.setText("Steward :" + waiterName + "\n");
                    posPrinterBuilder.setText("Punched By :" + waiterName + "\n");
                }
                posPrinterBuilder.setText("Kot No   :" + tokenNo + "\n");
                if(!StringUtils.isEmpty(orderNo)) {
                    posPrinterBuilder.setText("OrderNo :" + orderNo + "\n");
                }
                if(!StringUtils.isEmpty(pax)){
                    posPrinterBuilder.setText("Pax     :" + pax     + "\n");
                }
                posPrinterBuilder.chooseFont(4);
                posPrinterBuilder.setText("Date   :" + strDate + "\n");
                posPrinterBuilder.newLine();
                posPrinterBuilder.chooseFont(1);


                posPrinterBuilder.setText("--------------------------------------------------------");
                posPrinterBuilder.newLine();
                posPrinterBuilder.setText("   Item Name                               Qty     ");
                posPrinterBuilder.newLine();
                posPrinterBuilder.setText("--------------------------------------------------------\n");
                posPrinterBuilder.newLine();

                int index = 0;
                for (POSKOTItemOrderDTO order : kitchenOrders) {
                    double qty=order.getItemQty();
                    String quantity=null;
                    if((qty-(int)qty)!=0){
                        quantity=String.valueOf(qty);
                    }else {
                        quantity=String.valueOf((double) qty);
                    }
                    String decimalPattern = "([0-9]*)\\.([1-9]*)";
                    boolean match = Pattern.matches(decimalPattern, quantity);
                    if(match==true){
                        quantity=String.valueOf((double) qty);
                        double f = Double.parseDouble(quantity);
                        quantity= String.format("%.2f", new BigDecimal(f));
                    }else {
                        quantity=String.valueOf((int) qty);
                    }

                    posPrinterBuilder.chooseFont(9);
                    if(!order.getItemName().contains("CHARGE")) {
                        posPrinterBuilder.setText("  "
                                + String.format("%-30s", String.valueOf(order.getItemName().substring(0, Math.min(order.getItemName().length(), 30)))).toUpperCase() + ""
                                + String.format("%-3s", String.valueOf(quantity)) + ""
                                + "\n");
                    }
                    posPrinterBuilder.chooseFont(6);
                    if(!StringUtils.isEmpty(order.getItemDescription())) {
                        posPrinterBuilder.setText(String.format("%-18s", order.getItemDescription().length() <= 30?"("+String.valueOf(order.getItemDescription().substring(0,Math.min(order.getItemDescription().length(),30)))+")":"("+String.valueOf(order.getItemDescription().substring(0,Math.min(order.getItemDescription().length(),30)))) + "" + "\n");
                        if (order.getItemDescription().length() > 30) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-18s", order.getItemDescription().length() <= 61?String.valueOf(order.getItemDescription()).substring(31, Math.min(order.getItemDescription().length(), 61))+")":String.valueOf(order.getItemDescription()).substring(31, Math.min(order.getItemDescription().length(), 61))) + ""
                                    + "\n");
                        }
                        if (order.getItemDescription().length() > 61) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-18s", order.getItemDescription().length() <= 92?String.valueOf(order.getItemDescription()).substring(62, Math.min(order.getItemDescription().length(), 92))+")":String.valueOf(order.getItemDescription()).substring(62, Math.min(order.getItemDescription().length(), 92))) + ""
                                    + "\n");
                        }
                        if (order.getItemDescription().length() > 92) {
                            posPrinterBuilder.setText(""
                                    + String.format("%-18s", String.valueOf(order.getItemDescription()).substring(93, Math.min(order.getItemDescription().length(), 125)))+")" + ""
                                    + "\n");
                        }
                    }
                }
                if(!StringUtils.isEmpty(instructions)) {
                    posPrinterBuilder.newLine();
                    posPrinterBuilder.setText("Instructions: " + instructions);
                    posPrinterBuilder.newLine();
                }
                posPrinterBuilder.feed((byte) 3);
                posPrinterBuilder.finit();

                if (kitchenOrders != null && kitchenOrders.size() > 0) {
                    JSONObject cancelKOTPrinter = new JSONObject(accountSetupPojo.getBillModel());
                    if(orderType.equals("Order") || (orderType.equals("Cancel") && cancelKOTPrinter.optBoolean("reportToCategory"))) {
                        if(buildType.equals("cloud"))
                        {
                            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString("undefined"));
                            {
                                String printerName = jsonObject.getString("undefined");
                                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                                printerData = cloudPrinterConf.get(printerName);
                                if(printerData == null) {
                                    printerData = new ArrayList();
                                    cloudPrinterConf.put(printerName,printerData);
                                }
                                printerData.add(new String(data,"UTF-8"));
                            }

                        }
                        else {
                            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), jsonObject.getString("undefined"));
                        }
                    }
                    if(orderType.equals("Cancel") && cancelKOTPrinter.optBoolean("reportToReceipt")) {
                        if(buildType.equals("cloud"))
                        {
                            cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(),  jsonObject.getString("undefined"));
                            {
                                String printerName = accountSetupPojo.getReportPrinter();
                                byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                                printerData = cloudPrinterConf.get(printerName);
                                if(printerData == null) {
                                    printerData = new ArrayList();
                                    cloudPrinterConf.put(printerName,printerData);
                                }
                                printerData.add(new String(data,"UTF-8"));
                            }

                        }
                        else {
                            feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(),  jsonObject.getString("undefined"));
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cloudPrinterConf;
    }
    @SuppressWarnings("Duplicates")
    public Map formatAndPrintCustomerBill(String jsonString) {

        POSPrinterBuilder posPrinterBuilder = new POSPrinterBuilder();
        AccountSetup accountSetup=accountSetupRepository.findOne(1L);
        Gson gson = new Gson();
        Type type1 = new TypeToken<PrinterSelectionDisplayPojo>(){}.getType();
        PrinterSelectionDisplayPojo printerSelectionDisplayPojo = new PrinterSelectionDisplayPojo();
        printerSelectionDisplayPojo = gson.fromJson(accountSetup.getPrintDetails(),type1);
        cloudPrinterConf = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String str1 = jsonObject.getString("selectedItemList");
            List<SelectedItem> itemList = new Gson().fromJson(str1, new TypeToken<List<SelectedItem>>(){}.getType());

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy @ hh:mm a");
            String strDate = formatter.format(date);

            posPrinterBuilder.resetAll();
            posPrinterBuilder.initialize();
            posPrinterBuilder.feedBack((byte) 2);

            if(StringUtils.pathEquals(printerSelectionDisplayPojo.getFont(),"2")) {
                posPrinterBuilder.chooseFont(2);
            }else {
                posPrinterBuilder.chooseFont(3);
            }

            Company company = companyRepository.findAllByStatus("Active");
            //Company Name
            posPrinterBuilder.alignCenter();
                posPrinterBuilder.setText(company.getCompanyName().toUpperCase());
                posPrinterBuilder.newLine();
            UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
            posPrinterBuilder.chooseFont(2);
            //Description
            if(!StringUtils.isEmpty(printerSelectionDisplayPojo.getCompanyDescription())){
                posPrinterBuilder.setText(printerSelectionDisplayPojo.getCompanyDescription());
                posPrinterBuilder.newLine();
            }
            //Address
            if(!StringUtils.isEmpty(company.getAddress())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getAddress(),"Yes")) {
                posPrinterBuilder.setText(company.getAddress());
                posPrinterBuilder.newLine();
            }
            //pincode
            if(!StringUtils.isEmpty(company.getPincode())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getPincode(),"Yes")) {
                posPrinterBuilder.setText("Pincode: " + company.getPincode());
                posPrinterBuilder.newLine();
            }
            //Phone
            if(!StringUtils.isEmpty(company.getPhone())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getPhone(),"Yes")) {
                posPrinterBuilder.setText("Phone: " + company.getPhone());
                posPrinterBuilder.newLine();
            }
            //GSTIN
            if(!StringUtils.isEmpty(company.getGstNo())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getGstn(),"Yes")) {
                posPrinterBuilder.setText("GSTIN: " + company.getGstNo());
                posPrinterBuilder.newLine();
            }
            //Email
            if(!StringUtils.isEmpty(company.getEmail())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCompanyEmail(),"Yes")){
                posPrinterBuilder.setText("Email: " + company.getEmail());
                posPrinterBuilder.newLine();
            }
            //Date
            if(StringUtils.pathEquals(printerSelectionDisplayPojo.getDate(),"Yes")) {
                posPrinterBuilder.setText("Date: " + strDate);
                posPrinterBuilder.newLine();
            }
            //Invoice No.
            posPrinterBuilder.setText("Invoice Number:  * Customer Bill *");
            posPrinterBuilder.newLine();
            if(!accountSetup.isCopytokot()) {
                if (!StringUtils.isEmpty(jsonObject.getString("waiter"))&& StringUtils.pathEquals(printerSelectionDisplayPojo.getSteward(),"Yes")&&(!StringUtils.pathEquals(jsonObject.getString("tableName"),"Delivery")&&!StringUtils.pathEquals(jsonObject.getString("tableName"),"TAKEAWAY"))) {
                    posPrinterBuilder.setText("Steward:" + jsonObject.getString("waiter"));
                    posPrinterBuilder.newLine();
                }
                if(!StringUtils.isEmpty(jsonObject.getString("orderNo"))){
                    if(StringUtils.pathEquals(printerSelectionDisplayPojo.getAgent(),"Yes")) {
                        posPrinterBuilder.setText("Aggregator: " + jsonObject.getString("agent"));
                        posPrinterBuilder.newLine();
                    }
                    if(StringUtils.pathEquals(printerSelectionDisplayPojo.getOrderId(),"Yes")) {
                        posPrinterBuilder.setText("Order No: " + jsonObject.getString("orderNo"));
                        posPrinterBuilder.newLine();
                    }
                }else {
                    if(StringUtils.pathEquals(printerSelectionDisplayPojo.getTable(),"Yes")) {
                        posPrinterBuilder.setText("Table No: " + jsonObject.getString("tableName"));
                        posPrinterBuilder.newLine();
                    }
                }
            }else {
                if(StringUtils.pathEquals(printerSelectionDisplayPojo.getTable(),"Yes")) {
                    posPrinterBuilder.setText("Table No: " + "Walkin");
                    posPrinterBuilder.newLine();
                }
            }

            if(!StringUtils.isEmpty(jsonObject.getString("TokenNos"))&& StringUtils.isEmpty(jsonObject.getString("orderNo"))) {
                if(StringUtils.pathEquals(printerSelectionDisplayPojo.getTokenNo(),"Yes")) {
                    posPrinterBuilder.setText("Kot No: " + jsonObject.getString("TokenNos"));
                    posPrinterBuilder.newLine();
                }
            }
            if(StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerName(),"Yes")) {
                posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
                posPrinterBuilder.setText("    Customer Details:   ");
                posPrinterBuilder.newLine();
                posPrinterBuilder.setText("Customer Name:" + jsonObject.getString("customerName"));
                posPrinterBuilder.newLine();
            }
            Customer customer=customerRepository.findAllByCustomerId(Long.valueOf(jsonObject.getString("customerId")));
            if(!StringUtils.isEmpty(customer.getPhoneNumber1())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerPhone(),"Yes")) {
                posPrinterBuilder.setText("Phone: " + customer.getPhoneNumber1());
                posPrinterBuilder.newLine();
            }
            if(!StringUtils.isEmpty(customer.getAddress())&& StringUtils.pathEquals(printerSelectionDisplayPojo.getCustomerAddress(),"Yes")) {
                posPrinterBuilder.setText("Address: "+customer.getAddress());
                posPrinterBuilder.newLine();
            }
//            if(!StringUtils.isEmpty(customer.getArAccount())&&StringUtils.pathEquals(printerSelectionDisplayPojo.getPincode(),"Yes")) {
//                posPrinterBuilder.setText("Pincode: "+customer.getArAccount());
//                posPrinterBuilder.newLine();
//            }
            posPrinterBuilder.chooseFont(2);

            posPrinterBuilder.alignCenter();
            //3-Inch Space Set
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText(" Name                    Price     Qty    Amount");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();

            double sumAmtExclusiveTax = 0;
            double totalTaxAmt = 0;
            double totalAmt = 0;
            double discountAmount = 0;
            //- means left justified
            for (SelectedItem item : itemList) {
                posPrinterBuilder.setText(""
                        + String.format("%-25s", String.valueOf(item.getItemName()).substring(0,Math.min(item.getItemName().length(),24)).toUpperCase()) + ""
                        + String.format("%-9s", String.format("%.02f", item.getUnitPrice())) + ""
                        + String.format("%-4s", String.format("%.02f", item.getQty())) + ""
                        + String.format("%10s", String.format("%.02f", item.getUnitPrice()*item.getQty())) + ""
                        + "\n");
                posPrinterBuilder.alignLeft();
                if(item.getItemName().length()>24){
                    posPrinterBuilder.setText(""
                            + String.format("%-25s", String.valueOf(item.getItemName()).substring(25,Math.min(item.getItemName().length(),48)).toUpperCase()) + ""
                            + "\n");
                }
                posPrinterBuilder.alignCenter();
                sumAmtExclusiveTax += item.getUnitPrice()*item.getQty();
                totalAmt += item.getAmtinclusivetax();
                totalTaxAmt += item.getTaxamt();
                discountAmount += item.getDiscountAmt();
            }
            List<TaxSummary> taxPerList = new ArrayList<>();
            for(SelectedItem selectedItem:itemList){
                selectedItem.setTaxpercent(Double.parseDouble(accountSetup.getTaxId()));
            }
            TaxSummary taxSummary = new TaxSummary();
//                taxSummary.setTaxName(tax.getTaxName());
            taxSummary.setTaxableAmt(Double.parseDouble(jsonObject.getString("grandtotal"))-Double.parseDouble(jsonObject.getString("totalTax")));
            taxSummary.setTaxAmount(Double.parseDouble(jsonObject.getString("totalTax")));
            taxSummary.setTaxPercent(Double.parseDouble(accountSetup.getTaxId()));
            taxSummary.setCessAmt(0);
            taxSummary.setCessPercent(0);
            taxPerList.add(taxSummary);
            double discountAmt=0,serviceCharge=0,discountPer=0,serviceChargeAmt=0;
            discountPer=Double.parseDouble(jsonObject.getString("discount"));
            discountAmt=Double.parseDouble(jsonObject.getString("discountAmt"));
            String discType=jsonObject.getString("discountType");
            serviceCharge=Double.parseDouble(jsonObject.getString("serviceCharge"));
            serviceChargeAmt=Double.parseDouble(jsonObject.getString("hiposServiceChargeAmt"));
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("Sub Total:                        " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax)) + "\n");
            if(!StringUtils.isEmpty(discType)&& StringUtils.pathEquals(discType,"Percentage")) {
                posPrinterBuilder.setText("Discount (" + String.format("%14s", String.format("%.00f", discountPer) + "%)" + ":                   "+ String.format("%14s", String.format("%.02f", discountAmt)) + "\n"));
            }else if(discountAmt>0) {
                posPrinterBuilder.setText("Discount :                        "+ String.format("%14s", String.format("%.02f", discountAmt)) + "\n");
            }

            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
            posPrinterBuilder.setText("Net Total:                        " + String.format("%14s", String.format("%.02f", sumAmtExclusiveTax-discountAmt )) + "\n");
            if (serviceChargeAmt != 0) {
                int i=0;
//                HiposServiceChargeDTO hiPosServiceCharge=hiposDAO.getServiceChargeListByNameOrCode(null,null,null,null,userAccountSetup).get(0);
//                i=15-hiPosServiceCharge.getServiceChargeName().length();
                if(i==0||i<0){
                    i=3;
                }else {
                    i=i+3;
                }
//                posPrinterBuilder.setText(hiPosServiceCharge.getServiceChargeName().substring(0,Math.min(hiPosServiceCharge.getServiceChargeName().length(),15))+"("+String.format("%.02f",serviceCharge)+"%) :           "+String.format("%"+i+"s","")+String.format("%11s", String.format("%.02f",serviceChargeAmt)+"\n"));
            }
            for(TaxSummary taxSummary1:taxPerList){
                posPrinterBuilder.setText("CGST ("+String.format("%.02f",taxSummary1.getTaxPercent()/2)+"%):                     " + String.format("%14s", String.format("%.02f", taxSummary1.getTaxAmount()/2)) + "\n");
                posPrinterBuilder.setText("SGST ("+String.format("%.02f",taxSummary1.getTaxPercent()/2)+"%):                     " + String.format("%14s", String.format("%.02f", taxSummary1.getTaxAmount()/2)) + "\n");
            }
            double roundOffValue=0;
            if(jsonObject.getString("roundingOffValue") != null) {
                roundOffValue = Double.parseDouble(jsonObject.getString("roundingOffValue"));
                if(roundOffValue!=0) {
                    if (roundOffValue >=0.5)
                        roundOffValue = Math.abs(roundOffValue);
                    else
                        roundOffValue = -1*roundOffValue;
                    posPrinterBuilder.setText("Rounding Off:                     " + String.format("%14s", String.format("%.02f", roundOffValue)) + "\n");
                }
            }
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.chooseFont(11);
            posPrinterBuilder.setText("Grand Total:                         " + String.format("%11s", String.format("%.02f", Double.parseDouble(jsonObject.getString("grandtotal"))+roundOffValue+serviceChargeAmt)) + "\n");
            posPrinterBuilder.setText("------------------------------------------------");
            posPrinterBuilder.newLine();
            posPrinterBuilder.alignCenter();
            if(!StringUtils.isEmpty(printerSelectionDisplayPojo.getFooterText())) {
                posPrinterBuilder.setText(printerSelectionDisplayPojo.getFooterText());
                posPrinterBuilder.newLine();
            }
            posPrinterBuilder.setText("Designed & Powered By RestoPos");

            posPrinterBuilder.feed((byte) 3);
            posPrinterBuilder.finit();

            AccountSetupPojo accountSetupPojo = hiposService.getConfigureDetails();
            TablesPos tablesPos=null;
            TableConfig tableConfig=null;
            List<PosPrinterDto> posPrinterDtos = new ArrayList<>();
            Type type = new TypeToken<List<PosPrinterDto>>() {
            }.getType();
            String printersName = null;
            if (!StringUtils.isEmpty(accountSetup.getReportPrinter())) {
                posPrinterDtos = gson.fromJson(accountSetup.getReportPrinter(), type);
                if (!StringUtils.isEmpty(jsonObject.getString("tableNo"))) {
                    tablesPos = hiposService.getTablePosObj(jsonObject.getString("tableNo"));
                    tableConfig = tableConfigRepository.findAllByConfigurationname(tablesPos.getConfigurationname());
                    for (PosPrinterDto posPrinterDto : posPrinterDtos) {
                        if(tableConfig!=null&&userAccountSetup!=null)
                            if(StringUtils.pathEquals(posPrinterDto.getTableConfig(),tableConfig.getTableconfigid().toString())&& StringUtils.pathEquals(posPrinterDto.getUser(),userAccountSetup.getFullName())){
                                printersName = posPrinterDto.getPosPrinter();
                            }
                    }
                }
                if (StringUtils.isEmpty(printersName)) {
                    printersName = posPrinterDtos.get(0).getPosPrinter();
                }
            }
            if(buildType.equals("cloud"))
            {
                cloudFeedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), accountSetupPojo.getReportPrinter());
                {
                    byte[] data = posPrinterBuilder.finalCommandSet().getBytes();
                    printerData = cloudPrinterConf.get(printersName);
                    if(printerData == null) {
                        printerData = new ArrayList();
                        cloudPrinterConf.put(printersName,printerData);
                    }
                    try {
                        printerData.add(new String(data, "UTF-8"));
                    }catch(UnsupportedEncodingException e){e.printStackTrace();}
                }
            }
            else{
                feedPrinter(posPrinterBuilder.finalCommandSet().getBytes(), printersName);
            }
        }catch(Exception e){e.printStackTrace();}
        return cloudPrinterConf;
    }



}


