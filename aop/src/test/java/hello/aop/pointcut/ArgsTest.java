package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.assertThat;

import hello.aop.member.MemberServiceImpl;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * execution vs args 차이
 * execution:   파라미터 타입이 정확하게 매칭되어야 한다. 클래스에 선언된 정보를 기반으로 판단한다.
 * args:        부모 타입을 허용한다. 실제 넘어온 파라미터 객체 인스턴스를 보고 판단한다.
 */
@Slf4j
public class ArgsTest {

    Method helloMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() {
        // hello(String)과 매칭
        assertThat(pointcut("args(String)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args()")
            .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("args(..)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(*)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(String, ..)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * execution(* *(java.io.Serializable)): 메서드 시그니처로 판단 (정적)
     * args(java.io.Serializable): 런타임에 전달된 인수로 판단 (동적)
     */
    @Test
    void argsVsExecution() {
        // [args]
        assertThat(pointcut("args(String)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        // String은 Serializable을 구현하고 있다.
        assertThat(pointcut("args(java.io.Serializable)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // [execution]
        assertThat(pointcut("execution(* *(String))")
            .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(java.io.Serializable))")
            .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *(Object))")
            .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
