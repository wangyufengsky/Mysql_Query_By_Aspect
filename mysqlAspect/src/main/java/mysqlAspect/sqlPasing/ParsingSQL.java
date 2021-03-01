package mysqlAspect.sqlPasing;

import lombok.extern.slf4j.Slf4j;
import mysqlAspect.jdbc.JdbcDeal;
import mysqlAspect.jdbc.JdbcEntity;
import mysqlAspect.utils.LambdaUtils;
import mysqlAspect.utils.SQLUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ParsingSQL<T>{


    public Map<String,List<String>> parsingSQL(JdbcEntity<T> entity) throws Exception{
        Map<String,List<String>> dbMap=new HashMap<>();
        String sql=entity.getSql().toUpperCase();
        int sqlType= SQLUtils.getType(sql);
        Map<String, Map<String, List<JdbcDeal<T>>>> groupByDeals=entity.getGroupByDeals();
        switch (sqlType){
            case 0 -> groupByDeals.forEach((k,v)->{
                List<String> tbList=new ArrayList<>();
                v.forEach((k1,v1)->tbList.add(parsingInsert(sql,v1)));
                dbMap.put(k,tbList);
            });
            case 1 -> groupByDeals.forEach((k,v)->{
                List<String> tbList=new ArrayList<>();
                v.forEach((k1,v1)->tbList.addAll(parsingSelect(sql,v1)));
                dbMap.put(k,tbList);
            });
            case 2-> groupByDeals.forEach((k,v)->{
                List<String> tbList=new ArrayList<>();
                v.forEach((k1,v1)->tbList.add(parsingUpdate(sql,v1)));
                dbMap.put(k,tbList);
            });
        }
        return dbMap;
    }


    //ex: INSERT INTO @table (name,age,num) values (@name,@age,@num);
    private String parsingInsert(String input,List<JdbcDeal<T>> inputEntities){
        if(inputEntities.size()==0||!input.contains("VALUES")){
            return "";
        }
        String table=inputEntities.get(0).getDatabase()+"."+inputEntities.get(0).getTable();
        input=input.replaceFirst("@TABLE",table);
        String[] sqls=input.split("VALUES");
        StringBuffer sql=new StringBuffer(sqls[0].trim()).append(" values (");
        String[] fields=dealSql(sqls);
        AtomicInteger time= new AtomicInteger();
        try {
            inputEntities.forEach(LambdaUtils.throwingConsumerWrapper(jdbcDeal->{
                if(time.get()!=0){
                    sql.append(",(");
                }
                AtomicInteger times= new AtomicInteger();
                Arrays.stream(fields).forEach(LambdaUtils.throwingConsumerWrapper(field->{
                    if(times.get()!=0){
                        sql.append(",");
                    }
                    if(field.startsWith("@")){
                        sql.append(getField(jdbcDeal,field.replace("@","")));
                    }else {
                        sql.append(field);
                    }
                    times.getAndIncrement();
                    if(times.get()==fields.length){
                        sql.append(")");
                    }
                }));
                time.getAndIncrement();
            }));
            sql.append(";");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return sql.toString();
    }



    //ex: SELECT NAME FROM @table WHERE age=@age and num=@num;
    private List<String> parsingSelect(String input,List<JdbcDeal<T>> inputEntities){
        if(inputEntities.size()!=1){
            return new ArrayList<>();
        }
        List<String> sqls=new ArrayList<>();
        String table=inputEntities.get(0).getDatabase()+"."+inputEntities.get(0).getTable();
        input=input.replaceFirst("@TABLE",table).replace(";","");
        List<String> list=dealSql(input);
        String finalInput = input;
        inputEntities.forEach(LambdaUtils.throwingConsumerWrapper(entry->{
            String sql=finalInput;
            for(String s:list){
                sql=sql.replace(s,getField(entry,s.replace("@","")));
            }
            sqls.add(sql);
        }));
        return sqls;
    }



    private String parsingUpdate(String input,List<JdbcDeal<T>> inputEntities){
        return "TODO Update";
    }


    private String[] dealSql(String[] sqls){
        return sqls[1].trim().replace(";","").replace("(","").replace(")","").split(",");
    }

    private List<String> dealSql(String input){
        List<String> list=new ArrayList<>();
        String[] inputs=input.split(" ");
        Arrays.stream(inputs).filter(i->i.contains("@")).forEach(in->{
            if(in.startsWith("@")){
                list.add(in);
            }else if(in.contains("=")){
                list.add(in.split("=")[1]);
            }
        });
        return list;
    }


    private String getField(JdbcDeal<T> jdbcDeal, String field) throws Exception{
        T entry=jdbcDeal.getEntity();
        List<Field> fields=Arrays.stream(entry.getClass().getDeclaredFields()).filter(f->f.getName().toUpperCase().equals(field)).collect(Collectors.toList());
        if(fields.size()==0){
            log.error("字段不匹配");
            throw new Exception("字段不匹配");
        }
        Field f=fields.get(0);
        f.setAccessible(true);
        if(f.getGenericType().equals(String.class)){
            return "'"+f.get(entry).toString()+"'";
        }else return f.get(entry).toString();
    }




}
