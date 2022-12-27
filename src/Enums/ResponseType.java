package Enums;

/**
 * Responses that could be sent by the database node.
 */
public enum ResponseType {
    ok("OK"),
    error("ERROR");

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
