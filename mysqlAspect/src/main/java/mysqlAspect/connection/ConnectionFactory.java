package mysqlAspect.connection;

import mysqlAspect.datasource.DataSource;
import mysqlAspect.utils.LambdaUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectionFactory {


    public static Connection createConnection(DataSource dataSource) throws SQLException {
        return dataSource.getDataSource().getConnection();
    }

    public static List<Connection> createConnections(List<DataSource> dataSources){
        return dataSources
                .parallelStream()
                .map(LambdaUtils.throwingFunctionWrapper(dataSource->dataSource.getDataSource().getConnection()))
                .collect(Collectors.toList());
    }

    public static Map<String,Connection> createConnectionMap(Map<String,DataSource> dataSources){
        Map<String,Connection> connectionMap=new HashMap<>();
        dataSources.forEach(LambdaUtils.throwingBiConsumerWrapper((k,v)->connectionMap.put(k,v.getDataSource().getConnection())));
        return connectionMap;
    }

    public static Map<String,Connection> createConnectionMap(List<DataSource> dataSources){
        Map<String,Connection> connectionMap=new HashMap<>();
        dataSources.parallelStream().forEach(LambdaUtils.throwingConsumerWrapper(dataSource->connectionMap.put(dataSource.getDbName(),dataSource.getDataSource().getConnection())));
        return connectionMap;
    }

}
