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
        productRepository.save(new Product("A", 10000, 10));
        productRepository.save(new Product("B", 20000, 20));

        memberRepository.save(new Member("tester1", "testID1", "testPW1"));
        memberRepository.save(new Member("tester2", "testID2", "testPW2"));
    }
}
