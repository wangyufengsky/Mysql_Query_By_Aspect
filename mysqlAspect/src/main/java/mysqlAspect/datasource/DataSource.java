package mysqlAspect.datasource;

import com.alibaba.druid.pool.DruidDataSource;

public class DataSource {

    private String ip;
    private String dbName;
    private String port;
    private String userName;
    private String passWord;
    private Long maxWait;
    private int maxActive;
    private int initialSize;
    private boolean keepAlive;
    private boolean removeAbandoned;

    private DruidDataSource dataSource;

    public static DataSourcesBuilder builder() {
        return new DataSourcesBuilder();
    }

    public static final class DataSourcesBuilder {
        private String ip;
        private String dbName;
        private String port;
        private String userName;
        private String passWord;
        private Long maxWait;
        private int maxActive=10;
        private int initialSize=0;
        private boolean keepAlive=true;
        private boolean removeAbandoned=false;

        private final DruidDataSource dataSource=new DruidDataSource();

        public DataSourcesBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public DataSourcesBuilder port(String port) {
            this.port = port;
            return this;
        }

        public DataSourcesBuilder name(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DataSourcesBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public DataSourcesBuilder passWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public DataSourcesBuilder maxWait(Long maxWait) {
            this.maxWait = maxWait;
            return this;
        }

        public DataSourcesBuilder maxActive(int maxActive) {
            this.maxActive = maxActive;
            return this;
        }

        public DataSourcesBuilder initialSize(int initialSize) {
            this.initialSize = initialSize;
            return this;
        }

        public DataSourcesBuilder keepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public DataSourcesBuilder removeAbandoned(boolean removeAbandoned) {
            this.removeAbandoned = removeAbandoned;
            return this;
        }

        public DataSource build() {
            DataSource dataSources = new DataSource();
            dataSources.dbName = this.dbName;
            dataSources.initialSize = this.initialSize;
            dataSources.maxActive = this.maxActive;
            dataSources.maxWait = this.maxWait;
            dataSources.keepAlive = this.keepAlive;
            dataSources.ip = this.ip;
            dataSources.userName = this.userName;
            dataSources.passWord = this.passWord;
            dataSources.port = this.port;
            dataSources.removeAbandoned=this.removeAbandoned;
            this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            this.dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true",this.ip,this.port,this.dbName));
            this.dataSource.setUsername(this.userName);
            this.dataSource.setPassword(this.passWord);
            this.dataSource.setMaxWait(null==this.maxWait?10:this.maxWait);
            this.dataSource.setMaxActive(this.maxActive);
            this.dataSource.setInitialSize(this.initialSize);
            this.dataSource.setKeepAlive(this.keepAlive);
            this.dataSource.setRemoveAbandoned(this.removeAbandoned);
            dataSources.dataSource=this.dataSource;
            return dataSources;
        }
    }

    public String getIp() {
        return ip;
    }

    public String getDbName() {
        return dbName;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public DruidDataSource getDataSource() {
        return dataSource;
    }
}
