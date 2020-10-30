package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Category;
import com.hyva.restopos.rest.pojo.CategoryPojo;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category mapPojoToEntity (CategoryPojo categoryPojo){
        Category category = new Category();
        category.setItemCategoryId(categoryPojo.getItemCategoryId());
        category.setItemCategoryCode(categoryPojo.getItemCategoryCode());
        category.setItemCategoryName(categoryPojo.getItemCategoryName());
        category.setDefaultType(categoryPojo.getDefaultType());
        category.setItemCategoryDesc(categoryPojo.getItemCategoryDesc());
        category.setLocationId(categoryPojo.getLocationId());
        category.setStatus(categoryPojo.getStatus());
        category.setUseraccount_id(categoryPojo.getUseraccount_id());
        return category;
    }

    public static List<CategoryPojo> mapEntityToPojo(List<Category> List) {
        List<CategoryPojo> list = new ArrayList<>();
        for (Category config : List) {
            CategoryPojo categoryPojo = new CategoryPojo();
            categoryPojo.setItemCategoryId(config.getItemCategoryId());
            categoryPojo.setItemCategoryCode(config.getItemCategoryCode());
            categoryPojo.setItemCategoryName(config.getItemCategoryName());
            categoryPojo.setDefaultType(config.getDefaultType());
            categoryPojo.setItemCategoryDesc(config.getItemCategoryDesc());
            categoryPojo.setLocationId(config.getLocationId());
            categoryPojo.setStatus(config.getStatus());
            categoryPojo.setUseraccount_id(config.getUseraccount_id());
            list.add(categoryPojo);
        }
        return list;
    }

}
