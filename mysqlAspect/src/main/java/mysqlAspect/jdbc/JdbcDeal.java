package mysqlAspect.jdbc;


public class JdbcDeal<T> {

    private T entity;
    private String database;
    private String table;


    public static final class Builder<T> {
        private T entity;
        private String database;
        private String table;

        public Builder<T> entity(T entity) {
            this.entity = entity;
            return this;
        }

        public Builder<T> database(String database) {
            this.database = database;
            return this;
        }

        public Builder<T> table(String table) {
            this.table = table;
            return this;
        }

        public JdbcDeal<T> build() {
            JdbcDeal<T> jdbcDeal = new JdbcDeal<>();
            jdbcDeal.database=this.database;
            jdbcDeal.entity=this.entity;
            jdbcDeal.table=this.table;
            return jdbcDeal;
        }
    }

    public T getEntity() {
        return entity;
    }

    public String getDatabase() {
        return database;
    }

    public String getTable() {
        return table;
    }

    @Override
    public String toString() {
        return "JdbcDeal{" +
                "entity=" + entity +
                ", database='" + database + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
