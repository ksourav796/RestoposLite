package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long itemCategoryId;
    private String itemCategoryCode;
    private String itemCategoryName;
    private String itemCategoryDesc;
    private String locationId;
    private String useraccount_id;
    private String status;
    private String defaultType;
}
