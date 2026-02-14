package com.cinema.model.dao.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class QueryBuilder {
    public static boolean insert(String sql, List<Object> parameters) throws SQLException {
        try (PreparedStatement stmt = setStatement(sql, parameters)) {
            return stmt.execute();
        }
    }

    public static int insertGetId(String sql, List<Object> parameters) throws SQLException {
        try (PreparedStatement stmt = setStatement(sql, parameters)) {
            stmt.execute();

            ResultSet last_insert = stmt.getResultSet();

            if (last_insert.next()) {
                return last_insert.getInt(1);
            }

            return 0;
        }
    }

    public static boolean insertBatch(String sql, List<List<Object>> batchParameters) throws SQLException {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (List<Object> parametersList: batchParameters) {
            setParameters(stmt, parametersList);
        }

        return false;
    }

    public static PreparedStatement prepareStatement(String sql, List<Object> parameters) throws SQLException {
        return setStatement(sql, parameters);
    }

    private static PreparedStatement setStatement(String sql, List<Object> parameters) throws SQLException {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement(sql);
        setParameters(stmt, parameters);
        return stmt;
    }

    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            Object param = parameters.get(i);
            int parametersIndex = i + 1;

            if (param == null) {
                stmt.setNull(parametersIndex, Types.NULL);
            } else if (param instanceof String) {
                stmt.setString(parametersIndex, (String) param);
            } else if (param instanceof Integer) {
                stmt.setInt(parametersIndex, (Integer) param);
            } else if (param instanceof Double) {
                stmt.setDouble(parametersIndex, (Double) param);
            } else if (param instanceof Boolean) {
                stmt.setBoolean(parametersIndex, (Boolean) param);
            } else if (param instanceof LocalDateTime) {
                stmt.setTimestamp(parametersIndex, Timestamp.valueOf((LocalDateTime) param));
            } else {
                stmt.setObject(parametersIndex, param);
            }
        }
    }
}
