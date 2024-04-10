package SpringMVC.productservice3.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)      //파라미터에만 사용한다.
@Retention(RetentionPolicy.RUNTIME) //런타임까지 애노테이션 정보가 남아있는다 ==> 리플렉션 등의 활용 가능
public @interface Login {
}
