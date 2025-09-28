package main.java.enums;

public enum Currency {
    MAD,
    EUR,
    USD ;

    public static Currency fromString(String code) {
        if (code == null) return EUR;
        for (Currency c : Currency.values()) {
            if (c.name().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return EUR;
    }


}
