package com.appify.vidstream.newWebApi.data;

import org.apache.log4j.Logger;

import com.appify.vidstream.newWebApi.CategoryResp;
import com.appify.vidstream.newWebApi.LoadAppServlet;

public class CategoryToCategoryRespConverter {

	static final Logger categoryToCategoryRespConverterLogger = Logger.getLogger(CategoryToCategoryRespConverter.class);
	
	 public CategoryResp getCategoryRespFromCategoryWithoutChild(Category category){
		 CategoryResp categoryResp = new CategoryResp();
		 categoryResp.setId(category.getId());
		 categoryResp.setName(category.getName());
		 categoryResp.setImgURL(category.getImageURL());
	     return categoryResp;  
	    }
}
