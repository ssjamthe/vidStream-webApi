package com.appify.vidstream.newWebApi.data;

/**
 * Created by swapnil on 09/01/17.
 */
public class CategorizationToCategoryConverter {
    private static final String ID_PREFIX = "Catz";

    public static Category convert(Categorization categorization)
    {
        Category category = new Category();
        category.setChildren(categorization.getChildren());
        category.setChildType(categorization.getChildType());
        category.setName(categorization.getName());
        category.setId(ID_PREFIX + categorization.getId());
        category.setImageURL(categorization.getImageURL());

        return category;
    }
}
