# MysqlAspect——mysql分库分表中间件

由于京东的shardingjdbc效率太低，所以之前做大数据的时候自己写了一套mysql分库分表算法，现在将其公共化成为中间件，就叫做MysqlAspect吧
代码示例：
           
           
           public class Test {
               public static void main(String[] args) {
                   //batchInsert();
                   select();
               }
    
               private static void batchInsert(){
                   DataSource dataSource0= DataSource.builder().ip("10.141.57.59").port("3306").name("db_0").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource1= DataSource.builder().ip("10.141.57.59").port("3306").name("db_1").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource2= DataSource.builder().ip("10.141.57.59").port("3306").name("db_2").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource3= DataSource.builder().ip("10.141.57.59").port("3306").name("db_3").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource4= DataSource.builder().ip("10.141.57.59").port("3306").name("db_4").userName("root").passWord("123456").maxWait((long) 10000).build();
                   List<DataSource> list= Stream.of(dataSource0,dataSource1, dataSource2,dataSource3,dataSource4).collect(Collectors.toList());
                   //String sql="select name from @table where age=@age";
                   String sql="INSERT INTO @table (name,age,num) values (@name,@age,@num);";
                   System.out.println(sql);
                   TestBean testBean1=new TestBean("cao","23","11");
                   TestBean testBean2=new TestBean("wang","24","22");
                   TestBean testBean3=new TestBean("liu","11","23");
                   TestBean testBean4=new TestBean("li","2","34");
                   TestBean testBean5=new TestBean("pan","3","14");
                   TestBean testBean6=new TestBean("dong","4","34");
                   TestBean testBean7=new TestBean("lou","1","33");
                   TestBean testBean8=new TestBean("ma","2","34");
                   TestBean testBean9=new TestBean("sui","2","34");
                   TestBean testBean0=new TestBean("fu","2","34");
                   List<TestBean> beans= Stream.of(testBean1, testBean2,testBean3,testBean4,testBean5,testBean6,testBean7,testBean8,testBean9,testBean0).collect(Collectors.toList());
                   JdbcEntity<TestBean> entity=new JdbcEntity.JdbcEntityBuilder<TestBean>()
                           .sql(sql)
                           .entities(beans)
                           .dataSources(list)
                           .shardingBaseAlgorithm(new StringAlgorithm<>(testBean -> "db_" + Integer.parseInt(testBean.getAge()) % 10, testBean -> "testTable_" + Integer.parseInt(testBean.getNum()) % 10))
                           .build();
                   System.out.println(entity.toString());
                   entity.getInputDeals().forEach(s-> System.out.println(s.toString()));

                   entity.doInsert();
               }
    
               private static void select(){
                   DataSource dataSource0= DataSource.builder().ip("10.141.57.59").port("3306").name("db_0").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource1= DataSource.builder().ip("10.141.57.59").port("3306").name("db_1").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource2= DataSource.builder().ip("10.141.57.59").port("3306").name("db_2").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource3= DataSource.builder().ip("10.141.57.59").port("3306").name("db_3").userName("root").passWord("123456").maxWait((long) 10000).build();
                   DataSource dataSource4= DataSource.builder().ip("10.141.57.59").port("3306").name("db_4").userName("root").passWord("123456").maxWait((long) 10000).build();
                   List<DataSource> list= Stream.of(dataSource0,dataSource1, dataSource2,dataSource3,dataSource4).collect(Collectors.toList());
                   String sql="select name from @table where age=@age and num=@num";
                   TestBean testBean1=new TestBean("","23","11");
                   TestBean testBean2=new TestBean("","24","22");
                   TestBean testBean3=new TestBean("","11","23");
                   List<TestBean> beans= Stream.of(testBean1, testBean2,testBean3).collect(Collectors.toList());
                   JdbcEntity<TestBean> entity=new JdbcEntity.JdbcEntityBuilder<TestBean>()
                           .sql(sql)
                           .entities(beans)
                           .dataSources(list)
                           .shardingBaseAlgorithm(new StringAlgorithm<>(testBean -> "db_" + Integer.parseInt(testBean.getAge()) % 10, testBean -> "testTable_" + Integer.parseInt(testBean.getNum()) % 10))
                           .build();
                   System.out.println(entity.toString());
                   entity.getInputDeals().forEach(s-> System.out.println(s.toString()));
                   List<TestBean> resultSets=entity.doSelect();
                   resultSets.forEach(System.out::println);
               }
           }          
