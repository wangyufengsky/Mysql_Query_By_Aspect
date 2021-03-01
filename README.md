# MysqlAspect——mysql分库分表中间件

由于京东的shardingjdbc效率太低，所以之前做大数据的时候自己写了一套mysql分库分表算法，现在将其公共化成为中间件，就叫做MysqlAspect吧
代码示例：
           
           
           public class Test {
                public static void main(String[] args) {
                    DataSource dataSource1= DataSource.builder().ip("10.119.10.102").port("3306").name("db_1").userName("root").passWord("123456").maxWait((long) 10000).build();
                    DataSource dataSource2= DataSource.builder().ip("10.119.10.102").port("3306").name("db_2").userName("root").passWord("123456").maxWait((long) 10000).build();
                    DataSource dataSource3= DataSource.builder().ip("10.119.10.102").port("3306").name("db_3").userName("root").passWord("123456").maxWait((long) 10000).build();
                    List<DataSource> list= Stream.of(dataSource1, dataSource2,dataSource3).collect(Collectors.toList());
                    //String sql="select name from @table where age=@age";
                    String sql="INSERT INTO @table ('name','age','num') values (@name,@age,@num) ;";
                    System.out.println(sql);
                    TestBean testBean1=new TestBean("cao",23,12);
                    TestBean testBean2=new TestBean("wang",24,22);
                    TestBean testBean3=new TestBean("liu",15,23);
                    TestBean testBean4=new TestBean("li",6,34);
                    List<TestBean> beans= Stream.of(testBean1, testBean2,testBean3,testBean4).collect(Collectors.toList());
                    JdbcEntity<TestBean> entity=new JdbcEntity.JdbcEntityBuilder<TestBean>()
                            .sql(sql)
                            .entities(beans)
                            .dataSources(list)
                            .shardingBaseAlgorithm(new StringAlgorithm<>(testBean -> "db_" + testBean.getAge() % 10, testBean -> "testTable_" + testBean.getNum() % 10))
                            .build();
                    System.out.println(entity.toString());
                    entity.getInputDeals().forEach(s-> System.out.println(s.toString()));
            
                    entity.doInsert();
                }
            }