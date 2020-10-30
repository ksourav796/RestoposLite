package com.hyva.restopos.rest.pojo;

import lombok.Data;


@Data
public class TablesPosPojo {
    private Long tableid;
    private String tableName;
    private int gridLocationH;
    private int gridLocationV;
    private String configurationname;
    private String tableConfig;
    private String Status;
    private Long noOfChairs;
    private String locationId;
    private String useraccount_id;
    private String tableStatus;
    private String mergeTable;
    private String minCapacity;
    private String maxCapacity;
    private String floorId;
    private String message;
}
