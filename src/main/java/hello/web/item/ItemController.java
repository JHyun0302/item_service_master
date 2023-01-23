package hello.web.item;

import hello.domain.item.itemService.DeliveryCode;
import hello.domain.item.itemService.Item;
import hello.domain.item.itemService.ItemRepository;
import hello.domain.item.itemService.ItemType;
import hello.web.item.form.ItemSaveForm;
import hello.web.item.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemRepository itemRepository;

    /**
     * 상품 add, edit, item에 모두  Map<String, String> regions = new LinkedHashMap<>(); 해야함! (코드 중복!)
     *
     * @Controller 시작할 때 자동으로 model에 addAttribute됨
     */

    @ModelAttribute("regions") //자바 코드
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>(); //hashMap은 순서 보장이 안됨!
        regions.put("SEOUL", "서울"); //"SEOUL": 시스템상 key, "서울": 사용자에게 보이는 값
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions; //model.addattribute("regions", regions) 자동으로 모델에 담김
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values(); //enum의 모든 정보를 배열로 반환: [BOOK, FOOD, ETC]
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "items/addForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setOpen(form.getOpen());
        item.setRegions(form.getRegions());
        item.setItemType(form.getItemType());
        item.setDeliveryCode(form.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("editItem") ItemUpdateForm form, BindingResult bindingResult) {
        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/editForm";
        }
        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setQuantity(form.getQuantity());
        itemParam.setPrice(form.getPrice());
        itemParam.setOpen(form.getOpen());
        itemParam.setRegions(form.getRegions());
        itemParam.setItemType(form.getItemType());
        itemParam.setDeliveryCode(form.getDeliveryCode());

        itemRepository.update(itemId, itemParam);
        return "redirect:/items/{itemId}";
    }
}
