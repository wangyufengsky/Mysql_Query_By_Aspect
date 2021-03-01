package mysqlAspect.algorithm;


public abstract class ShardingBaseAlgorithm<T>{

    protected ShardingFunction<T, Exception> databaseMapper;

    protected ShardingFunction<T, Exception> tableMapper;

    public ShardingBaseAlgorithm(ShardingFunction<T, Exception> databaseMapper,ShardingFunction<T, Exception> tableMapper) {
        this.tableMapper = tableMapper;
        this.databaseMapper = databaseMapper;
    }

    public abstract String goTable(T t);

    public abstract String goDatabase(T t);



}
