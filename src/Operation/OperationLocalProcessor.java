package Operation;

import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Record;

public class OperationLocalProcessor {

    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<Node> connectedNodes;

    public OperationLocalProcessor(IConcurrentOperationalStorage<Record> recordStorage,
                                   IConcurrentStorage<Node> connectedNodes) {
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
    }

    /**
     * OK
     */
    public void connect(Node node) {
        connectedNodes.add(node);
    }

    /**
     * OK
     */
    public void disconnect(Node node) {
        connectedNodes.remove(node);
    }

    /**
     * OK or ERROR
     * @param alterRecord
     */
    public boolean setValue(Record alterRecord) {
        if (recordStorage.get(alterRecord.getKey()) != null) {
            recordStorage.add(alterRecord);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Record or ERROR
     */
    public Record getValue(int key) {
        return recordStorage.get(key);
    }

    /**
     * Node (self) or ERROR
     */
    public boolean findKey(int key) {
        return recordStorage.get(key) != null;
    }

    /**
     * Record or ERROR
     */
    public Record getMax() {
        if (recordStorage.count() == 0) {
            return null;
        }
        return recordStorage.max();
    }

    /**
     * Record or ERROR
     */
    public Record getMin() {
        if (recordStorage.count() == 0) {
            return null;
        }
        return recordStorage.min();
    }

    /**
     * OK
     */
    public void newRecord(Record newRecord) {
        recordStorage.add(newRecord);
    }

    /**
     * OK
     */
    public void terminate() {
        // No local processing of this operation
    }
}
