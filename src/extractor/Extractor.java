package extractor;

import java.util.Map;

public interface Extractor <K, V> {
    Map<K, V> extract(String inputPath);
}
