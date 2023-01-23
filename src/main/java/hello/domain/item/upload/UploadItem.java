package hello.domain.item.upload;

import lombok.Data;

import java.util.List;

@Data
public class UploadItem {
    private Long id;
    private String itemName;
    private UploadFile attachFile; //고객이 업로드한 파일명
    private List<UploadFile> imageFiles;
}
