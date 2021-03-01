package mysqlAspect.algorithm;

@FunctionalInterface
public interface ShardingFunction <T,E extends Exception>{
    String apply(T t)throws E;
}
