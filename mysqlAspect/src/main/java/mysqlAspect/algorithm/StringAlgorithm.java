package mysqlAspect.algorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringAlgorithm<T> extends ShardingBaseAlgorithm<T> {

    public StringAlgorithm(ShardingFunction<T,Exception> databaseMapper, ShardingFunction<T,Exception> tableMapper) {
        super(databaseMapper, tableMapper);
    }

    @Override
    public String goTable(T bean) {
        String result="";
        try {
            result=this.tableMapper.apply(bean);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public String goDatabase(T bean) {
        String result="";
        try {
            result=this.databaseMapper.apply(bean);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
