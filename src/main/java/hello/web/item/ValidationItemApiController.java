package hello.web.item;

import hello.web.item.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm item, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출"); ///tpyeMissMatch한 경우 ItemSaveitem자체가 안 만들어져서 컨트롤러 자체가 안 만들어짐

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }// bindingResult가 가지고 있는 모든 오류들(object & field error) json형식(@RestController)으로 return

        log.info("성공 로직 실행");
        return item;
    }
}
