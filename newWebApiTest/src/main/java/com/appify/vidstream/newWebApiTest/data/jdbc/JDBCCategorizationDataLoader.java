package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.Categorization;
import com.appify.vidstream.newWebApiTest.data.EntityType;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by swapnil on 23/11/16.
 */
public class JDBCCategorizationDataLoader {

    private DataSource dataSource;

    @Inject
    JDBCCategorizationDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<Categorization> getCategorizationsForApp(String appId) {

        try (Connection con = dataSource.getConnection();) {
            String sql = "select id,name,image from categorization where app_id='" + appId + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String image = rs.getString("image");

                Categorization categorization = new Categorization();
                categorization.setId(id);
                categorization.setName(name);
                categorization.setImageId(image);
                categorization.setChildType(EntityType.CATEGORY);
            }

            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting categorization data.", ex);
        }
    }
}
