package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.CategoryPrefixHelper;
import org.apache.log4j.Logger;

import com.appify.vidstream.newWebApi.CategoryResp;
import com.appify.vidstream.newWebApi.LoadAppServlet;

public class CategoryToCategoryRespConverter {

    static final Logger categoryToCategoryRespConverterLogger = Logger.getLogger(CategoryToCategoryRespConverter.class);

    public CategoryResp getCategoryRespFromCategoryWithoutChild(String categoryIdPrefix,Category category) {
        CategoryResp categoryResp = new CategoryResp();
        categoryResp.setId(CategoryPrefixHelper.addPrefixToId(categoryIdPrefix, category.getId()));
        categoryResp.setName(category.getName());
        categoryResp.setImgURL(category.getImageURL());
        return categoryResp;
    }
}
