package mysqlAspect.jdbc;

import lombok.extern.slf4j.Slf4j;
import mysqlAspect.utils.LambdaUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class JdbcDoing<T>{

    private final Map<String, Connection> connectionMap;
    private final Map<String,List<String>> lists;

    public JdbcDoing(Map<String, Connection> connectionMap,Map<String,List<String>> lists) {
        this.connectionMap = connectionMap;
        this.lists = lists;
    }


    public void jdbcInsert(){
        lists.forEach((k,v)->{
            Connection connection=connectionMap.get(k);
            try (Statement statement = connection.createStatement()) {
                v.forEach(LambdaUtils.throwingConsumerWrapper(statement::addBatch));
                statement.executeBatch();
                log.info("批量更新成功,笔数:"+v.size());
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
    }


    public List<T> jdbcSelect(T t){
        List<T> resultList=new ArrayList<>();
        lists.forEach((k,v)->{
            Connection connection=connectionMap.get(k);
            try (Statement statement = connection.createStatement()) {
                v.forEach(LambdaUtils.throwingConsumerWrapper(sql->{
                    T t1=(T)t.getClass().newInstance();
                    ResultSet resultSet=statement.executeQuery(sql);
                    setEntry(resultList,resultSet,t1);
                }));
                log.info("批量查询成功,查询笔数:"+v.size());
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return resultList;
    }


    private void setEntry(List<T> entry,ResultSet resultSet,T t){
        try {
            while(resultSet.next()){
                Field[] fields= t.getClass().getDeclaredFields();
                Arrays.stream(fields).forEach(LambdaUtils.throwingConsumerWrapper(field -> {
                    field.setAccessible(true);
                    if(isExistColumn(resultSet,field.getName())){
                        field.set(t,resultSet.getObject(field.getName()));
                    }
                }));
                entry.add(t);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0 ) {
                return true;
            }
        }
        catch (SQLException e) {
            return false;
        }

        return false;
    }

}
