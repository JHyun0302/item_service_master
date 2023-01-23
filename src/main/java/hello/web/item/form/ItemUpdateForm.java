package hello.web.item.form;

import hello.domain.item.itemService.ItemType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemUpdateForm {
    @NotNull
    private Long id;

    @NotNull
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    private Integer quantity;

    private Boolean open; //판매 여부
    private List<String> regions; //등록 지역
    private ItemType itemType;//상품 종류
    private String deliveryCode;//배송 방식
}
