package com.hyva.restopos.rest.pojo;

import lombok.Data;

@Data
public class TableConfigPojo {
    private Long tableconfigid;
    private int rowtableconfig;
    private String configurationname;
    private String status;
    private int columntableconfig;
}
