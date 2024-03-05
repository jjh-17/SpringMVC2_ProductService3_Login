package SpringMVC.productservice3;

import SpringMVC.productservice3.domain.repository.MemberRepository;
import SpringMVC.productservice3.domain.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import SpringMVC.productservice3.domain.member.Member;
import SpringMVC.productservice3.domain.product.Product;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    //테스트용 데이터 추가
    @PostConstruct
    public void testInit() {
        productRepository.save(Product.builder().name("A").price(10000).quantity(10).build());
        productRepository.save(Product.builder().name("B").price(20000).quantity(20).build());


        memberRepository.save(Member.builder().name("tester1").loginId("testID1").password("testPW1").build());
        memberRepository.save(Member.builder().name("tester2").loginId("testID2").password("testPW2").build());
    }
}
