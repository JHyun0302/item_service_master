package hello.domain.item.upload;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UploadFileRepository {
    private final Map<Long, UploadItem> store = new HashMap<>();

    private long sequence = 0L;

    public UploadItem save(UploadItem uploadItem) {
        uploadItem.setId(++sequence);
        store.put(uploadItem.getId(), uploadItem);
        return uploadItem;
    }

    public UploadItem findById(Long id) {
        return store.get(id);
    }
}
