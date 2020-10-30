package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class ShiftMapper {

    public static Shift MapPojoToEntity(ShiftPojo shiftPojo){
        Shift shift = new Shift();
        shift.setShiftId(shiftPojo.getShiftId());
        shift.setShiftName(shiftPojo.getShiftName());
        shift.setFromTime(shiftPojo.getFromTime());
        shift.setToTime(shiftPojo.getToTime());
        shift.setStatus(shiftPojo.getStatus());
        return shift;
    }
    public static List<ShiftPojo> mapShiftEntityToPojo(List<Shift> List) {
        List<ShiftPojo> list = new ArrayList<>();
        for (Shift shift : List) {

            ShiftPojo shiftPojo = new ShiftPojo();
            shiftPojo.setShiftId(shift.getShiftId());
            shiftPojo.setShiftName(shift.getShiftName());
            shiftPojo.setFromTime(shift.getFromTime());
            shiftPojo.setToTime(shift.getToTime());
            shiftPojo.setStatus(shift.getStatus());
            list.add(shiftPojo);
        }
        return list;
    }

}
