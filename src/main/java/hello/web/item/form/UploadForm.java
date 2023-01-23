package hello.web.item.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadForm {
    private Long itemId;
    private String itemName;
    private List<MultipartFile> imageFiles; //이미지 다중 업로드
    private MultipartFile attachFile;
}

