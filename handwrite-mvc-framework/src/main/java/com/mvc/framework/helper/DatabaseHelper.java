package com.mvc.framework.helper;

import com.mvc.framework.bean.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DatabaseHelper 为数据库操作助手类,
 * 可以通过该助手类进行增删改查, 事务等一系列的数据库操作.
 * @author study
 * @create 2019-11-13 20:42
 */
public class DatabaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DatabaseHelper.class);
    //连接池
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    //查询运行
    private static final QueryRunner QUERY_RUNNER;
    //数据源
    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER=new ThreadLocal<Connection>();
        QUERY_RUNNER =new QueryRunner();

        DATA_SOURCE =new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
    }
    /**
     * 获取数据源
     */
    public static BasicDataSource getDataSource() {
        return DATA_SOURCE;
    }
    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if(connection==null){
            //如果连接等于空
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }
    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection connection = getConnection();
        if(connection!=null){
            try {
                //更改为手动提交
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }

        }
    }
    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection connection = getConnection();
        if(connection!=null){
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }

        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if(connection!=null){
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }

        }

    }
    /**
     * 查询实体
     *
     * QUERY_RUNNER.query：
     * 将结果集的第一行数据,封装成JavaBean对象
     * qr.query(con, sql, new BeanHandler<Sort>(Sort.class));
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        Connection connection = getConnection();
        try {
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询实体列表
     * new BeanListHandler<T>(entityClass)
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;

        try {
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entityList failure", e);
            throw new RuntimeException(e);
        }
        return entityList;

    }
    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int update(String sql, Object... params) {
        //完成了几行
        int rows;

        try {
            Connection connection = getConnection();
            rows = QUERY_RUNNER.update(connection,sql,params);

        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;

    }
    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        //INSERT INTO Test01(ss,s2) VALUES (?,?)
        String columns ="";
        String values ="";
        for (Map.Entry<String, Object> stringObject : fieldMap.entrySet()) {
            columns+= stringObject.getKey()+",";
            values+= "?,";
        }
        columns = columns.substring(0,columns.length()-1);
        values = values.substring(0,values.length()-1);
        String sql = "INSERT INTO " + entityClass.getSimpleName() +"("+columns+") VALUES ("+values+")" ;

        return update(sql,fieldMap.values().toArray())==1;
    }
    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }
        //UPDATE Test01 SET ss = ?, s2 = ? WHERE id = ?
        //拼接字符串
        String columns="";
        for (Map.Entry<String, Object> stringObject : fieldMap.entrySet()) {
            columns+=stringObject.getKey()+" = ? ,";
        }
        columns=columns.substring(0,columns.length()-1);
        String sql ="UPDATE "+entityClass.getSimpleName()+" SET "+columns+" WHERE id="+id;

        List<Object> params =new ArrayList<>();
        params.addAll(fieldMap.values());
        params.add(id);

        return update(sql,params.toArray())==1;
    }
    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id)   {
        String sql="DELETE FROM "+entityClass.getSimpleName()+" WHERE id="+id;

        return update(sql,id)==1;

    }
}
