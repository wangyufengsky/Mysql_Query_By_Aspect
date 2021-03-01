package mysqlAspect.utils;

import mysqlAspect.enums.SQLEnum;

public class SQLUtils {

    public static int getType(String sql){
        if(sql.toUpperCase().contains("INSERT")){
            return SQLEnum.INSERT.getCode();
        }else if(sql.toUpperCase().contains("UPDATE")){
            return SQLEnum.UPDATE.getCode();
        }else if(sql.toUpperCase().contains("SELECT")){
            return SQLEnum.SELECT.getCode();
        }else return -1;
    }
}
