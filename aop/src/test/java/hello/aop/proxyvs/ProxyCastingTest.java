package hello.aop.proxyvs;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);// jdk 동적 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시를  프록시를 구현클레스로 캐스팅 실패
        assertThatThrownBy(() -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);// CGLIB 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // CGLIB 프록시를  프록시를 구현클레스로 캐스팅 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
