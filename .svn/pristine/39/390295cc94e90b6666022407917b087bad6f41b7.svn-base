package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.TablesPos;
import com.hyva.restopos.rest.pojo.TablesPosPojo;

import java.util.ArrayList;
import java.util.List;

public class TablesPosMapper {

    public static TablesPos MapTablesPosPojoToEntity(TablesPosPojo tablesPosPojo){
        TablesPos tablesPos = new TablesPos();
        tablesPos.setTableid(tablesPosPojo.getTableid());
        tablesPos.setTableName(tablesPosPojo.getTableName());
        tablesPos.setGridLocationH(tablesPosPojo.getGridLocationH());
        tablesPos.setGridLocationV(tablesPosPojo.getGridLocationV());
        tablesPos.setConfigurationname(tablesPosPojo.getConfigurationname());
        tablesPos.setStatus(tablesPosPojo.getStatus());
        tablesPos.setNoOfChairs(tablesPosPojo.getNoOfChairs());
        tablesPos.setTableStatus("Empty");
        tablesPos.setMergeTable(tablesPosPojo.getMergeTable());
        tablesPos.setMaxCapacity(tablesPosPojo.getMaxCapacity());
        tablesPos.setMinCapacity(tablesPosPojo.getMinCapacity());

        return tablesPos;
    }

    public static List<TablesPosPojo> mapEntityToPojo(List<TablesPos> List) {
        List<TablesPosPojo> list = new ArrayList<>();
        for (TablesPos config : List) {
            TablesPosPojo tableConfigPojo = new TablesPosPojo();
            tableConfigPojo.setTableid(config.getTableid());
            tableConfigPojo.setTableName(config.getTableName());
            tableConfigPojo.setGridLocationH(config.getGridLocationH());
            tableConfigPojo.setGridLocationV(config.getGridLocationV());
            tableConfigPojo.setConfigurationname(config.getConfigurationname());
            tableConfigPojo.setStatus(config.getStatus());
            tableConfigPojo.setNoOfChairs(config.getNoOfChairs());
            tableConfigPojo.setTableStatus(config.getTableStatus());
            tableConfigPojo.setMergeTable(config.getMergeTable());
            tableConfigPojo.setMaxCapacity(config.getMaxCapacity());
            tableConfigPojo.setMinCapacity(config.getMinCapacity());
            list.add(tableConfigPojo);
        }
        return list;
    }
}
