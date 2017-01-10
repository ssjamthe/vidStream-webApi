package com.appify.vidstream.newWebApi;

/**
 * Created by swapnil on 09/01/17.
 */
public class CategoryPrefixHelper {

    private static final String PREFIX_ID_DILIMITER = "_";

    public static class CategoryIdData {
        private String prefix;
        private String categoryId;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static String addPrefixToId(String prefix, String categoryId) {
        return prefix + PREFIX_ID_DILIMITER + categoryId;
    }

    public static CategoryIdData getCategoryIdData(String prefixedCategory) {
        CategoryIdData data = new CategoryIdData();
        String splits[] = prefixedCategory.split(PREFIX_ID_DILIMITER);

        data.setPrefix(splits[0]);
        data.setCategoryId(splits[1]);

        return data;
    }
}
