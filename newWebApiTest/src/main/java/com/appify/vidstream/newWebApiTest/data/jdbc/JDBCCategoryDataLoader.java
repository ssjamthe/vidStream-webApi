package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.Category;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 24/11/16.
 */
public class JDBCCategoryDataLoader {

    private DataSource dataSource;

    @Inject
    JDBCCategoryDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Category> getCategoriesForCategorization(String categorizationId) {

        try (Connection con = dataSource.getConnection();) {
            List<Category> categories = new ArrayList<Category>();

            //Getting top level categories.
            String sql = "select id,name,image from category where categorization_id='"
                    + categorizationId + "' and id not in (select child_category_id from parent_child_category_mappings)";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String image = rs.getString("image");

                Category category = new Category();
                category.setId(id);
                category.setName(name);
                category.setImageId(image);
            }

            return categories;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categories data.", ex);
        }

    }

    private void setChildren(Category category) {
        try (Connection con = dataSource.getConnection();) {

            String sql = "select id,name,image from category where id in (select child_category_id " +
                    "from parent_child_category_mappings where parent_category_id='" + category.getId() + "')";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {

            }

        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categories data.", ex);
        }
    }
}
