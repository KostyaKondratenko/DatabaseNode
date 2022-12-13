package Entity;

public class Record {

    // region Variables
    private final int key;
    private final int value;
    // endregion

    public Record(int key, int value) {
        this.key = key;
        this.value = value;
    }

    // region Getters & Setters
    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
    // endregion


    @Override
    public String toString() {
        return key + ":" + value;
    }
}
