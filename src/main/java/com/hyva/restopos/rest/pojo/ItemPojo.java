package com.hyva.restopos.rest.pojo;

import lombok.Data;
@Data
public class ItemPojo {
    private long itemId;
    private String itemCode;
    private String itemAccountCode;
    private String itemName;
    private String itemDesc;
    private String certificateno;
    private String conTentType;
    private String fileName;
    private String serializableStatus;
    private String itemCategoryName;
    private double unitPrice;
    private double unitPriceIn;
    private String idItemCategory;
    private boolean editableDesc;
    private boolean excludeSales;
    private String serialIteamCount;
    private String imageFile;
    private String itemStatus;
    private String foodtype;
    private double stock;
    private double reorderlevel;
    private double stdCost;
    private String productionName;
    private double itemQuantityProduction;
    private double itemPrice;
    private String inclusiveJSON;
    private byte[] imageBlob;
    private float CESS;
    private String attributeJSON;
    private String tableDetails;
    private String cartStatus;
    private String cartValue;
    private String serviceDescription;
    private Long itemCategoryId;

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getIdItemCategory() {
        return idItemCategory;
    }

    public void setIdItemCategory(String idItemCategory) {
        this.idItemCategory = idItemCategory;
    }
}
