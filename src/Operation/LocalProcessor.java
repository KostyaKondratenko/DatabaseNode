package Operation;

import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Record;

/**
 * A class that processes an operation locally in the current node.
 * @see Entity.Operation
 * @see Enums.OperationType
 */
public class LocalProcessor {

    /** A list of records stored in the current node */
    private IConcurrentOperationalStorage<Record> recordStorage;
    /** A list of connected nodes to the current node */
    private IConcurrentStorage<Node> connectedNodes;

    public LocalProcessor(IConcurrentOperationalStorage<Record> recordStorage,
                          IConcurrentStorage<Node> connectedNodes) {
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
    }

    /**
     * Saves provided node to a list of connected nodes.
     * @param node a new node to connect to current node.
     * @see Node
     */
    public void connect(Node node) {
        connectedNodes.add(node);
    }

    /**
     * Removes provided node from a list of connected nodes.
     * @param node a new node to connect to current node.
     * @see Node
     */
    public void disconnect(Node node) {
        connectedNodes.remove(node);
    }

    /**
     * Sets a new value to an existing record in the current node.
     * @param alterRecord a new record to store in current node.
     * @return the boolean result of the operation. If the key from provided record exists in the storage -
     * than the record is updated and the result is TRUE, otherwise the result is FALSE.
     * @see Record
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
     * Returns a record by the provided key if it exists in the storage, otherwise returns null
     * @param key a key to search in the storage of the current node.
     * @return A record found in the storage or null.
     * @see Record
     */
    public Record getValue(int key) {
        return recordStorage.get(key);
    }

    /**
     * Finds a record by the provided key.
     * <p> The result of this operation is a message consisting of a pair {@code <key>:<value>}
     * identifying this node or ERROR if no node has a key with such a value. </p>
     * @param key a key to search in the storage of the current node.
     * @return A boolean result indicating whether the record was found in the current node or not.
     */
    public boolean findKey(int key) {
        return recordStorage.get(key) != null;
    }

    /**
     * Finds a maximum value in the current node's storage.
     * @return a record with maximum value or null if the current storage is empty.
     * @see Record
     */
    public Record getMax() {
        if (recordStorage.count() == 0) {
            return null;
        }
        return recordStorage.max();
    }

    /**
     * Finds a minimum value in the current node's storage.
     * <p> The result is a pair consisting of {@code <key>:<value>} pair. </p>
     * @return a record with minimum value or null if the current storage is empty.
     * @see Record
     */
    public Record getMin() {
        if (recordStorage.count() == 0) {
            return null;
        }
        return recordStorage.min();
    }

    /**
     * Adds a new value to the current node's storage.
     * <p> The result of this operation is OK response </p>
     * @param newRecord a new record to store in current node.
     * @see Record
     */
    public void newRecord(Record newRecord) {
        recordStorage.add(newRecord);
    }
}
