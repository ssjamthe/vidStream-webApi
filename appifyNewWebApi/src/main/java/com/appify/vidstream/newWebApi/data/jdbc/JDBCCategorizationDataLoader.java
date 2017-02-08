package com.appify.vidstream.newWebApi.data.jdbc;

import com.appify.vidstream.newWebApi.data.Categorization;
import com.appify.vidstream.newWebApi.data.Category;
import com.appify.vidstream.newWebApi.data.Entity;
import com.appify.vidstream.newWebApi.data.EntityType;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 23/11/16.
 */
public class JDBCCategorizationDataLoader {

    private DataSource dataSource;
    private JDBCCategoryDataLoader categoryDataLoader;
    private WebAPIUtil webAPIUtil;

    @Inject
    JDBCCategorizationDataLoader(DataSource dataSource, JDBCCategoryDataLoader categoryDataLoader, WebAPIUtil
            webAPIUtil) {
        this.dataSource = dataSource;
        this.categoryDataLoader = categoryDataLoader;
        this.webAPIUtil = webAPIUtil;
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
                categorization.setImageURL(webAPIUtil.getImageURL(image));
                categorization.setChildType(EntityType.CATEGORY);

                List<Category> categories = categoryDataLoader.getCategoriesForCategorization(Integer.toString(id));
                categorization.setChildren(ImmutableList.copyOf(categories.stream().map(c -> (Entity) c).collect(Collectors.toList())));

                categorizations.add(categorization);
            }

            return categorizations;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categorization data.", ex);
        }
    }

    public Map<String, Categorization> getCategorizationsMapForApp(String appId) {

        try (Connection con = dataSource.getConnection();) {
            Map<String, Categorization> categorizationMap = new HashMap<String, Categorization>();
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
                categorization.setImageURL(webAPIUtil.getImageURL(image));
                categorization.setChildType(EntityType.CATEGORY);

                List<Category> categories = categoryDataLoader.getCategoriesForCategorization(Integer.toString(id));
                categorization.setChildren(ImmutableList.copyOf(categories.stream().map(c -> (Entity) c).collect
                        (Collectors.toList())));

                categorizationMap.put(categorization.getId(), categorization);

            }

            return categorizationMap;

        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categorization data.", ex);
        }
    }
}
