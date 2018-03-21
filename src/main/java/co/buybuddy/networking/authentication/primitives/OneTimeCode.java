package co.buybuddy.networking.authentication.primitives;

public class OneTimeCode {
    private String code;

    public OneTimeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
