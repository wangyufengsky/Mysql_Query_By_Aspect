package mysqlAspect.jdbc;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcGroupBy<T> {


    public Map<String,Map<String,List<JdbcDeal<T>>>> jdbcGroupBy(List<JdbcDeal<T>> inputDeals){
        Map<String,Map<String,List<JdbcDeal<T>>>> map=new HashMap<>();
        inputDeals.stream()
                .collect(Collectors.groupingBy(JdbcDeal::getDatabase))
                .forEach((k,v)-> map.put(k,v.stream().collect(Collectors.groupingBy(JdbcDeal::getTable))));
        return map;
    }


}
