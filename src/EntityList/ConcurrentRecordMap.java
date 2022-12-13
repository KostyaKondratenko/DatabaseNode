package EntityList;

import Entity.Record;
import Interfaces.IConcurrentOperationalStorage;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class ConcurrentRecordMap implements IConcurrentOperationalStorage<Record> {
    private final ConcurrentMap<Integer, Integer> savedRecords;

    public ConcurrentRecordMap() {
        savedRecords = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Record value) {
        savedRecords.put(value.getKey(), value.getValue());
    }

    @Override
    public void remove(Record value) {
        savedRecords.remove(value.getKey(), value.getValue());
    }

    @Override
    public Record get(int index) {
        Integer value = savedRecords.get(index);
        return new Record(index, value);
    }

    @Override
    public int count() {
        return savedRecords.size();
    }

    @Override
    public Record max() {
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : savedRecords.entrySet()) {
            if (maxEntry == null || entry.getValue()
                    .compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return new Record(maxEntry.getKey(), maxEntry.getValue());
    }

    @Override
    public Record min() {
        Map.Entry<Integer, Integer> minEntry = null;
        for (Map.Entry<Integer, Integer> entry : savedRecords.entrySet()) {
            if (minEntry == null || entry.getValue()
                    .compareTo(minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }
        return new Record(minEntry.getKey(), minEntry.getValue());
    }
}
