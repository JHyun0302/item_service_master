package hello.web.item;

import hello.domain.file.FileStore;
import hello.domain.item.upload.UploadFile;
import hello.domain.item.upload.UploadFileRepository;
import hello.domain.item.upload.UploadItem;
import hello.web.item.form.UploadForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {
    private final UploadFileRepository uploadFileRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute UploadForm uploadForm) {
        return "/upload/item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute UploadForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeFiles = fileStore.storeFiles(form.getImageFiles());

        //DB 저장
        UploadItem uploadItem = new UploadItem();
        uploadItem.setItemName(form.getItemName());
        uploadItem.setAttachFile(attachFile);
        uploadItem.setImageFiles(storeFiles);
        uploadFileRepository.save(uploadItem);

        redirectAttributes.addAttribute("itemId", uploadItem.getId());
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        UploadItem item = uploadFileRepository.findById(id);
        model.addAttribute("item", item);
        return "upload/item-view";
    }

    //    html에 image 띄우기(404에러 해결), 보안에 취약! 체크 로직 추가하기
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
//        "file:/Users/../ 3557c4a3-2ae4-42d8-9fb9-edd9b626aec6.png"
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        UploadItem item = uploadFileRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={]", uploadFileName);
        String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);//한글 깨짐 방지
        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\""; // 첨부파일 클릭시 다운로드 규약
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
