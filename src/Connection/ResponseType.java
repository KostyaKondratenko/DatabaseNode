package Connection;

public enum ResponseType {
    ok("OK"),
    error("ERROR"),
    unknown("UNKNOWN");

    private final String descr;

    ResponseType(String descr) {
        this.descr = descr;
    }

    public boolean hasSubstringIn(String value) {
        return value.contains(descr);
    }

    @Override
    public String toString() {
        return descr;
    }
}
