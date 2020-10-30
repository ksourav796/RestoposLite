package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class CategoryPojo {
    private Long itemCategoryId;
    private String itemCategoryCode;
    private String itemCategoryName;
    private String itemCategoryDesc;
    private String locationId;
    private String useraccount_id;
    private String status;
    private String defaultType;
}
