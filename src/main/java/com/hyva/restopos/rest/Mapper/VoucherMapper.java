package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.PaymentVoucher;
import com.hyva.restopos.rest.pojo.PaymentVoucherPojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoucherMapper {

    public static PaymentVoucher MapPojoToEntity(PaymentVoucherPojo paymentVoucherPojo)throws Exception{
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setVouId(paymentVoucherPojo.getVouId());
        paymentVoucher.setVocherCode(paymentVoucherPojo.getVocherCode());
        paymentVoucher.setStatus(paymentVoucherPojo.getStatus());
        paymentVoucher.setDiscountType(paymentVoucherPojo.getDiscountType());
        paymentVoucher.setDiscountAmount(paymentVoucherPojo.getDiscountAmount());
        paymentVoucher.setMaxDiscount(paymentVoucherPojo.getMaxDiscount());
        SimpleDateFormat sfmtDate = new SimpleDateFormat("yyyy-MM-dd");
        paymentVoucher.setFromDate(sfmtDate.parse(paymentVoucherPojo.getFromDate()));
        paymentVoucher.setToDate(sfmtDate.parse(paymentVoucherPojo.getToDate()));
        paymentVoucher.setMinBill(paymentVoucherPojo.getMinBill());
        paymentVoucher.setNoOfTimesValid(paymentVoucherPojo.getNoOfTimesValid());
        paymentVoucher.setDefaultVoucher(paymentVoucherPojo.getDefaultVoucher());
        return paymentVoucher;
    }

    public static List<PaymentVoucherPojo> mapEntityToPojo(List<PaymentVoucher> List) {
        List<PaymentVoucherPojo> list = new ArrayList<>();
        for (PaymentVoucher voucher : List) {
            PaymentVoucherPojo paymentVoucherPojo = new PaymentVoucherPojo();
            paymentVoucherPojo.setVouId(voucher.getVouId());
            paymentVoucherPojo.setVocherCode(voucher.getVocherCode());
            paymentVoucherPojo.setStatus(voucher.getStatus());
            SimpleDateFormat sfmtDate = new SimpleDateFormat("yyyy-MM-dd");
            paymentVoucherPojo.setFromDate(sfmtDate.format(voucher.getFromDate()));
            paymentVoucherPojo.setToDate(sfmtDate.format(voucher.getToDate()));
            paymentVoucherPojo.setDiscountType(voucher.getDiscountType());
            paymentVoucherPojo.setMaxDiscount(voucher.getMaxDiscount());
            paymentVoucherPojo.setDiscountAmount(voucher.getDiscountAmount());
            paymentVoucherPojo.setMinBill(voucher.getMinBill());
            paymentVoucherPojo.setDefaultVoucher(voucher.getDefaultVoucher());
            paymentVoucherPojo.setNoOfTimesValid(voucher.getNoOfTimesValid());
            list.add(paymentVoucherPojo);
        }
        return list;
    }


}
