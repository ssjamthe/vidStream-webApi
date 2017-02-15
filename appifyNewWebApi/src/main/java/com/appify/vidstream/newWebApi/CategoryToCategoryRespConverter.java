package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.Category;
import org.apache.log4j.Logger;

public class CategoryToCategoryRespConverter {

    private static final Logger categoryToCategoryRespConverterLogger = Logger.getLogger
            (CategoryToCategoryRespConverter.class);

    public CategoryResp getCategoryRespFromCategoryWithoutChild(String categoryIdPrefix, Category category) {
        CategoryResp categoryResp = new CategoryResp();
        categoryResp.setId(CategoryPrefixHelper.addPrefixToId(categoryIdPrefix, category.getId()));
        categoryResp.setName(category.getName());
        categoryResp.setImgURL(category.getImageURL());
        return categoryResp;
    }
}
