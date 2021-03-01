package mysqlAspect.jdbc;

import lombok.extern.slf4j.Slf4j;
import mysqlAspect.algorithm.ShardingBaseAlgorithm;
import mysqlAspect.connection.ConnectionFactory;
import mysqlAspect.datasource.DataSource;
import mysqlAspect.sqlPasing.ParsingSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JdbcEntity<T> {

    private String sql;
    private List<T> inputEntities;
    private Map<String, Connection> connectionMap;
    private ShardingBaseAlgorithm<T> shardingBaseAlgorithm;

    private List<JdbcDeal<T>> inputDeals;

    private Map<String,Map<String,List<JdbcDeal<T>>>> groupByDeals;

    public static final class JdbcEntityBuilder<T> {
        private String sql;
        private List<T> inputEntities;
        private Map<String, Connection> connectionMap;
        private ShardingBaseAlgorithm<T> shardingBaseAlgorithm;
        private List<DataSource> dataSources;

        public JdbcEntityBuilder<T> sql(String sql) {
            this.sql = sql;
            return this;
        }

        public JdbcEntityBuilder<T> entities(List<T> inputEntities) {
            this.inputEntities = inputEntities;
            return this;
        }


        public JdbcEntityBuilder<T> shardingBaseAlgorithm(ShardingBaseAlgorithm<T> shardingBaseAlgorithm) {
            this.shardingBaseAlgorithm = shardingBaseAlgorithm;
            return this;
        }

        public JdbcEntityBuilder<T> dataSources(List<DataSource> dataSources) {
            this.dataSources = dataSources;
            return this;
        }

        public JdbcEntity<T> build() {
            JdbcEntity<T> jdbcEntity = new JdbcEntity<>();
            jdbcEntity.sql = this.sql;
            jdbcEntity.inputEntities = this.inputEntities;
            this.connectionMap = ConnectionFactory.createConnectionMap(this.dataSources);
            jdbcEntity.connectionMap = this.connectionMap;
            jdbcEntity.shardingBaseAlgorithm = this.shardingBaseAlgorithm;
            jdbcEntity.inputDeals=this.inputEntities.stream()
                    .map(entity->new JdbcDeal.Builder<T>()
                            .entity(entity)
                            .database(this.shardingBaseAlgorithm.goDatabase(entity))
                            .table(this.shardingBaseAlgorithm.goTable(entity))
                            .build())
                    .collect(Collectors.toList());
            jdbcEntity.groupByDeals=new JdbcGroupBy<T>().jdbcGroupBy(jdbcEntity.inputDeals);
            return jdbcEntity;
        }
    }

    public List<JdbcDeal<T>> getInputDeals() {
        return inputDeals;
    }

    public String getSql() {
        return sql;
    }

    public Map<String, Map<String, List<JdbcDeal<T>>>> getGroupByDeals() {
        return groupByDeals;
    }

    public void doInsert(){
        ParsingSQL<T> parsingSQL=new ParsingSQL<>();
        try {
            Map<String,List<String>> dbList=parsingSQL.parsingSQL(this);
            new JdbcDoing(this.connectionMap,dbList).jdbcInsert();
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<T> doSelect(){
        ParsingSQL<T> parsingSQL=new ParsingSQL<>();
        List<T> resultList=new ArrayList<>();
        try {
            Map<String,List<String>> dbList=parsingSQL.parsingSQL(this);
            resultList=new JdbcDoing<T>(this.connectionMap,dbList).jdbcSelect();
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    public String toString() {
        return "JdbcEntity{" +
                "sql='" + sql + '\'' +
                ", inputEntities=" + inputEntities +
                ", connectionMap=" + connectionMap +
                ", shardingBaseAlgorithm=" + shardingBaseAlgorithm +
                ", inputDeals=" + inputDeals +
                ", groupByDeals=" + groupByDeals +
                '}';
    }
}
