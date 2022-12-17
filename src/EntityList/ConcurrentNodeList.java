package EntityList;

import Entity.Node;
import Interfaces.IConcurrentStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ConcurrentNodeList implements IConcurrentStorage<Node>, Iterable<Node> {
    private final List<Node> connectedNodes;

    public ConcurrentNodeList() {
        connectedNodes = new ArrayList<>();
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    @Override
    public void add(Node value) {
        synchronized (connectedNodes) {
            boolean contains = false;
            for (Node connected : connectedNodes) {
                contains = connected.equals(value);
            }
            if (!contains) {
                connectedNodes.add(value);
            }
        }
    }

    @Override
    public void remove(Node value) {
        synchronized (connectedNodes) {
            int removeIndex = -1;
            for (int i = 0; i < connectedNodes.size() && removeIndex == -1; ++i) {
                Node connectedNode = connectedNodes.get(i);
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
    public Iterator<Node> iterator() {
        synchronized (connectedNodes) {
            return connectedNodes.iterator();
        }
    }

    @Override
    public void forEach(Consumer<? super Node> action) {
        synchronized (connectedNodes) {
            connectedNodes.forEach(action);
        }
    }

    @Override
    public Spliterator<Node> spliterator() {
        synchronized (connectedNodes) {
            return connectedNodes.spliterator();
        }
    }
}
