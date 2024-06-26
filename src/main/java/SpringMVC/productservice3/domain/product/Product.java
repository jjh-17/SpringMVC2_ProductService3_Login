package SpringMVC.productservice3.domain.product;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    //기본 정보
    private Long id;
    private String name;
    private Integer price; //null이 할당될 수 있으므로 int 대신 Integer
    private Integer quantity;

    @Builder
    public Product(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
