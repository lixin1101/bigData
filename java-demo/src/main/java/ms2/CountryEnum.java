package ms2;


public enum CountryEnum {
    ONE(1, "奇"), TWO(2, "楚"),
    THREE(3, "燕"), FOUR(4, "赵"),
    FIVE(5, "魏"), SIX(6, "韩");

    private Integer retCode;
    private String reMassage;

    public Integer getRetCode() {
        return retCode;
    }

    public String getReMassage() {
        return reMassage;
    }

    CountryEnum(Integer retCode, String reMassage) {
        this.retCode = retCode;
        this.reMassage = reMassage;
    }

    public static CountryEnum foreach_CountryEnum(int index) {
        CountryEnum[] values = CountryEnum.values();
        for (CountryEnum element : values) {
            if (index == element.getRetCode()) {
                return element;
            }
        }
        return null;
    }
}
