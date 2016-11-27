package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.Categorization;
import com.appify.vidstream.newWebApiTest.data.Category;
import com.appify.vidstream.newWebApiTest.data.Entity;
import com.appify.vidstream.newWebApiTest.data.EntityType;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 23/11/16.
 */
public class JDBCCategorizationDataLoader {

    private DataSource dataSource;
    private JDBCCategoryDataLoader categoryDataLoader;

    @Inject
    JDBCCategorizationDataLoader(DataSource dataSource, JDBCCategoryDataLoader categoryDataLoader) {
        this.dataSource = dataSource;
        this.categoryDataLoader = categoryDataLoader;
    }


    public List<Categorization> getCategorizationsForApp(String appId) {

        try (Connection con = dataSource.getConnection();) {
            List<Categorization> categorizations = new ArrayList<>();
            String sql = "select id,name,image from categorization where app_id='" + appId + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String image = rs.getString("image");

                Categorization categorization = new Categorization();
                categorization.setId(Integer.toString(id));
                categorization.setName(name);
                categorization.setImageId(image);
                categorization.setChildType(EntityType.CATEGORY);

                List<Category> categories = categoryDataLoader.getCategoriesForCategorization(Integer.toString(id));
                categorization.setChildren(categories.stream().map(c -> (Entity) c).collect(Collectors.toList()));

                categorizations.add(categorization);
            }

            return categorizations;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categorization data.", ex);
        }
    }
}
