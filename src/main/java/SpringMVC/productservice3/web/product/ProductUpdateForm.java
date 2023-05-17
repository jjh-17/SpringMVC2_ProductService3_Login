package SpringMVC.productservice3.web.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductUpdateForm {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    //수정에서는 수량은 자유롭게 변경할 수 있다.
    @NotNull
    private Integer quantity;
}
