package com.codecool.shop.database;

import java.sql.*;
import java.util.List;

public class ConnectionHandler implements AutoCloseable {
    private Connection con;

    private void before() {
        try {
            con = DriverManager.getConnection(
                    Config.getDATABASE(),
                    Config.getDB_USER(),
                    Config.getDB_PASSWORD());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet process(String query, List<TypeCaster> listOfFieldLabels) throws SQLException {
        before();
        PreparedStatement pstm = con.prepareStatement(query);
        for (int i = 0; i < listOfFieldLabels.size(); i++){
            String content = listOfFieldLabels.get(i).getContent();
            if (listOfFieldLabels.get(i).isNumber()) {
                pstm.setInt(i, Integer.parseInt(content));
            } else {
                pstm.setString(i, content);
            }
        }
        return pstm.executeQuery();
    }

    public ResultSet process(String query) throws SQLException {
        before();
        PreparedStatement pstm = con.prepareStatement(query);
        return pstm.executeQuery();
    }

    @Override
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
