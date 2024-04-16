package fun.hzaw.commonbean.enums;


public enum LogicEnum {

    TRUE("Y", 1, true),
    FALSE("N", 0, false),
    ;


    private final String strFlag;

    private final int intFlag;

    private final boolean booleanFlag;

    public String getStrFlag() {
        return strFlag;
    }

    public int getIntFlag() {
        return intFlag;
    }

    public boolean isBooleanFlag() {
        return booleanFlag;
    }

    LogicEnum(String strFlag, int intFlag, boolean booleanFlag) {
        this.strFlag = strFlag;
        this.intFlag = intFlag;
        this.booleanFlag = booleanFlag;
    }
}
