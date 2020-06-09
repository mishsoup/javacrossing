package reader;

import java.util.Map;

public interface FileReader<K, V> {
    Map<K, V> extract(String inputPath);
}
