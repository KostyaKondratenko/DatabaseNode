package Operation;

public enum OperationParameterType {
    visited("visited");

    private final String descr;

    public String toString() {
        return descr;
    }

    OperationParameterType(String descr) {
        this.descr = descr;
    }
}
