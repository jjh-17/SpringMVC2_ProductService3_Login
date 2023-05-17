package SpringMVC.productservice3.web.controller;

import SpringMVC.productservice3.domain.product.Product;
import SpringMVC.productservice3.domain.repository.ProductRepository;
import SpringMVC.productservice3.web.product.ProductSaveForm;
import SpringMVC.productservice3.web.product.ProductUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
[Form 전송 객체 분리]
1. 폼 데이터 전달에 동일한 도메인 객체 사용
    -동일한 객체를 컨트롤러, 리포지토리까지 직접 전달 ==> 중간에 도메인 객체 생성이 없어 간단
    -수정 시 중복 가능, groups 필요 ==> 간단한 경우에만 적용 가능

2. 폼 데이터 전달에 별도의 객체 사용
    -별도의 폼 객체를 만들어 검증 중복 X
    -컨트롤러에서 도메인 객체를 생성하는 변환 과정이 추가됨
 */
@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "product/product";
    }

    //상품 등록 - 입력 폼 처리
    @GetMapping("/add")
    public String addForm(Model model) {
        //입력할 상품 등록 폼 전달
        model.addAttribute("product", new Product());
        return "product/addForm";
    }

    //WebDataBinder를 이용한 사용자 설정 validator 사용
    //@Validated: 대상에 대한 검증기를 실행하라 ==> 검증 클래스의 supports() 메서드를 이용하여 적합한 검증기를 찾아서 실행
    @PostMapping("/add")
    public String addProduct(@Validated @ModelAttribute("product") ProductSaveForm form,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        //전체 예외
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "product/addForm";
        }

        //Product 객체 생성
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setQuantity(form.getQuantity());

        Product savedProduct = productRepository.save(product);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/products/{id}";
    }

    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "product/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @Validated @ModelAttribute("product") ProductUpdateForm form,
                              BindingResult bindingResult) {

        //전체 예외
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",
                        new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "product/editForm";
        }

        //Product 객체 생성
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setQuantity(form.getQuantity());

        productRepository.update(id, product);
        return "redirect:/products/{id}";
    }

}
