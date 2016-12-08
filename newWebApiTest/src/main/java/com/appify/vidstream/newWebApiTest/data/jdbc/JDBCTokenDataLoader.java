package com.appify.vidstream.newWebApiTest.data.jdbc;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 04/12/16.
 */
public class JDBCTokenDataLoader {

    private DataSource dataSource;
    private JDBCCategoryDataLoader categoryDataLoader;

    @Inject
    JDBCTokenDataLoader(DataSource dataSource, JDBCCategoryDataLoader categoryDataLoader) {
        this.dataSource = dataSource;
        this.categoryDataLoader = categoryDataLoader;
    }

    public List<String> getTokensForApp(String appId){
        try (Connection con = dataSource.getConnection();) {
            List<String> tokens = new ArrayList<>();
            String sql = "select token from auth_token where app_id='" + appId + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String token = rs.getString("token");
                tokens.add(token);
            }

            return tokens;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting token data.", ex);
        }

    }
}
