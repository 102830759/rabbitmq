package com.hdsx.rabbitmq.util;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateJavaFileUtils {
    private String tName;
    private List<String> colnames = new ArrayList<>();
    private List<String> labelnames = new ArrayList<>();
    private List<String> colTypes = new ArrayList<>();
    private List<String> precision = new ArrayList<>();
    private List<String> scale = new ArrayList<>();

    private boolean importUtil = false;
    private boolean importSql = false;
    private boolean importMath = false;

    private static Connection con = null;
    private static Statement sql = null;
    private static ResultSet rs = null;

    private static String driver = "oracle.jdbc.OracleDriver";
    private static String url = "jdbc:oracle:thin:@211.101.37.253:1521:orcl";
    private static String user = "HENAN_RTDB";
    private static String password = "HENAN_RTDB";

    /**
     * 生成类实体
     *
     * @param tName
     */
    public void tableToEntity(String tName) {
        this.tName = tName;
        getTableStructure();
        String content = parse();
//        FilePushOut(content);
        System.out.println(content);
    }

    /**
     * 字段SQL
     *
     * @param tName
     */
    public void getSqlColumn(String tName) {
        this.tName = tName;
        getTableStructure();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < colnames.size(); i++) {
            sb.append(colnames.get(i) + ",");
        }
        int i = sb.lastIndexOf(",");
        String substring = sb.substring(0, i);
        System.out.println(substring);
    }

    // 插入SQL
    public void getInsertSql(String tName) {
        this.tName = tName;
        getTableStructure();
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO " + tName + "(<include refid=\"column\"/>) VALUES(\r\n");
        for (int i = 0; i < colnames.size(); i++) {
            sb.append("#{" + colnames.get(i).toLowerCase() + ",jdbcType=" + oracleSqlTypeToInsertType(colTypes.get(i), i) + "},\r\n");
        }
        int i = sb.lastIndexOf(",");
        String substring = sb.substring(0, i);
        substring = substring + (")");
        System.out.println(substring);
    }

    // 更新SQL
    public void getUpdateSql(String tName) {
        this.tName = tName;
        getTableStructure();
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE " + tName + " SET\r\n");
        for (int i = 0; i < colnames.size(); i++) {
            sb.append(colnames.get(i) + "=#{" + colnames.get(i).toLowerCase() + ",jdbcType=" + oracleSqlTypeToInsertType(colTypes.get(i), i) + "},\r\n");
        }
        int i = sb.lastIndexOf(",");
        String substring = sb.substring(0, i);
        System.out.println(substring);
    }

    public void FilePushOut(String content) {
        FileWriter fw = null;
        try { // 进行输出
            fw = new FileWriter(initcap(tName) + ".java");
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载表结构
     */
    public void getTableStructure() {
        // 数据连Connection获取
        con = getConnection();
        String sss = "select c.comments comments,  t.column_name, data_type,data_precision,data_scale from user_tab_columns t left join user_col_comments c on t.table_name=c.table_name and t.COLUMN_NAME = c.column_name where t.table_name = '" + tName + "'";
        try {
            sql = con.createStatement();
            rs = sql.executeQuery(sss);
            int i = 0;
            while (rs.next()) {
                labelnames.add(rs.getString(1));
                colnames.add(rs.getString(2));
                colTypes.add(rs.getString(3));
                precision.add(rs.getString(4));
                scale.add(rs.getString(5));
                if ("datetime".equals(colTypes.get(i))) {
                    importUtil = true;
                }
                if ("image".equals(colTypes.get(i)) || "text".equals(colTypes.get(i))) {
                    importSql = true;
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(con);
        }
    }

    /**
     * 解析处理(生成实体类主体代码)
     */
    private String parse() {
        StringBuffer sb = new StringBuffer();
        sb.append("\r\nimport java.io.Serializable;\r\n");
        if (importUtil) {
            sb.append("import java.util.Date;\r\n");
        }
        if (importSql) {
            sb.append("import java.sql.*;\r\n\r\n");
        }
        if (importMath) {
            sb.append("import java.math.*;\r\n\r\n");
        }
        sb.append("public class " + initcap(tName)
                + " implements Serializable {\r\n");
        for (int i = 0; i < colnames.size(); i++) {
            sb.append("/**\r\n");
            sb.append("* " + labelnames.get(i) + "\r\n");
            sb.append("*/\r\n");
            sb.append("@ApiModelProperty(\"" + labelnames.get(i) + "\")\r\n");
            sb.append("private  " + oracleSqlType2JavaType(colTypes.get(i), i) + " " + colnames.get(i).toLowerCase() + ";\r\n");
        }
        sb.append("}\r\n");
        return sb.toString();
    }

    /**
     * 把输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * Oracle
     *
     * @param sqlType
     * @return
     */
    private String oracleSqlType2JavaType(String sqlType, int i) {
        if (sqlType.equals("Long")) {
            return "Long";
        } else if (sqlType.equals("DOUBLE") || sqlType.equals("double precision")) {
            return "Double";
        } else if (sqlType.equals("NUMBER") || sqlType.equals("DECIMAL")
                || sqlType.equals("NUMERIC") || sqlType.equals("real")) {
            return "0".equals(scale.get(i)) ? "Integer" : "Double";
        } else if (sqlType.equals("VARCHAR") || sqlType.equals("VARCHAR2")
                || sqlType.equals("CHAR") || sqlType.equals("nvarchar")
                || sqlType.equals("nchar")) {
            return "String";
        } else if (sqlType.equals("DATETIME") || sqlType.equals("DATE")
                || sqlType.equals("TIMESTAMP(6)")) {
            return "Date";
        } else {
            return "String";
        }

    }

    /**
     * 生成插入语句
     *
     * @param sqlType
     * @param i
     * @return
     */
    private String oracleSqlTypeToInsertType(String sqlType, int i) {
        if (sqlType.equals("DATETIME") || sqlType.equals("DATE")
                || sqlType.equals("TIMESTAMP(6)")) {
            return "TIMESTAMP";
        } else if (sqlType.equals("VARCHAR2")) {
            return "VARCHAR";
        } else if (sqlType.equals("NUMBER") || sqlType.equals("DECIMAL")
                || sqlType.equals("NUMERIC") || sqlType.equals("REAL")) {
            return "0".equals(scale.get(i)) ? "INTEGER" : "DECIMAL";
        }
        return "VARCHAR";
    }

    /**
     * 自动获取jcbc链接
     */
    protected static Connection getConnection() {
        Connection localConnection = null;
        try {
            Class.forName(driver);
            localConnection = DriverManager.getConnection(
                    url, user, password);
            localConnection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localConnection;
    }

    /**
     * 释放jdbc链接
     *
     * @param conn
     */
    protected static void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String tableName = "T_TRAFFIC_CURRENT_EVENT";
        GenerateJavaFileUtils t = new GenerateJavaFileUtils();
//        t.tableToEntity(tableName);
//        t.getSqlColumn(tableName);
//        t.getInsertSql(tableName);
        t.getUpdateSql(tableName);
    }

}
