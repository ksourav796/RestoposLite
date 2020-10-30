package com.hyva.restopos.rest.entities;

import com.hyva.restopos.util.HiNextConstants;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long itemId;
    private String itemCode;
    private String itemName;
    private String itemDesc;
    @OneToOne
    @JoinColumn(name = "IdItemCategory")
    private Category idItemCategory;
    private String imageFile;
    private String itemStatus;
    private String foodtype;
    private String itemtype;
    private String hsnCode;
    private double stock;
    private double reorderlevel;
    private double stdCost;
    private String productionName;
    private double itemQuantityProduction;
    private double itemPrice;
    @Column(name="logo")/*Multiple definition so used*/
    private String inclusiveJSON;
}
