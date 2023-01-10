package EntityList;

import Entity.IdentifiableNode;
import Interfaces.IConcurrentStorage;

import java.util.*;
import java.util.function.Consumer;

public class ConcurrentNodeList implements IConcurrentStorage<IdentifiableNode>, Iterable<IdentifiableNode> {
    private final List<IdentifiableNode> connectedNodes;

    public ConcurrentNodeList() {
        connectedNodes = new ArrayList<>();
    }

    public List<IdentifiableNode> getConnectedNodes() {
        return connectedNodes;
    }

    @Override
    public void add(IdentifiableNode value) {
        synchronized (connectedNodes) {
            boolean contains = false;
            for (IdentifiableNode connected : connectedNodes) {
                contains = connected.equals(value);
            }
            if (!contains) {
                connectedNodes.add(value);
            }
        }
    }

    @Override
    public void remove(IdentifiableNode value) {
        synchronized (connectedNodes) {
            int removeIndex = -1;
            for (int i = 0; i < connectedNodes.size() && removeIndex == -1; ++i) {
                IdentifiableNode connectedNode = connectedNodes.get(i);
                if (connectedNode.equals(value)) {
                    removeIndex = i;
                }
            }
            if (removeIndex >= 0 && removeIndex < connectedNodes.size()) {
                connectedNodes.remove(removeIndex);
            }
        }
    }

    @Override
    public Iterator<IdentifiableNode> iterator() {
        synchronized (connectedNodes) {
            return connectedNodes.iterator();
        }
    }

    @Override
    public void forEach(Consumer<? super IdentifiableNode> action) {
        synchronized (connectedNodes) {
            connectedNodes.forEach(action);
        }
    }

    @Override
    public Spliterator<IdentifiableNode> spliterator() {
        synchronized (connectedNodes) {
            return connectedNodes.spliterator();
        }
    }

    @Override
    public String toString() {
        return connectedNodes.toString();
    }
}
