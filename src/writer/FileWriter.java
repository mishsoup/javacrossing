package writer;

import java.util.Map;

public interface FileWriter<K, V> {
    void writeFiles(Map<K, V> contents);
}
