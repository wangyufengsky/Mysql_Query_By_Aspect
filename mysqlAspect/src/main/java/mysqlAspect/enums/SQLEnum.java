package mysqlAspect.enums;

public enum SQLEnum {

    INSERT(0,"insert"),SELECT(1,"select"),UPDATE(2,"update");

    private final int code;
    private final String type;

    SQLEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
