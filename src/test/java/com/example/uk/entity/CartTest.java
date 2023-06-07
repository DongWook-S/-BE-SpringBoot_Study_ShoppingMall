package com.example.uk.entity;

import com.example.uk.dto.MemberFormDto;
import com.example.uk.repository.CartRepository;
import com.example.uk.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    // 회원 엔티티를 생성하는 메소드
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("부산");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // JPA 는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 FLUSH() 를 호출하여 DB 에 반영.
                    // 회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로 FLUSH() 를 호출하여 DB 에 반영

        em.clear(); // JPA 는 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우 DB 를 조회
                    // 실제 DB 에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고 오는지 보기 위해서 영속성 컨텍스트를 비움

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new); // 저장된 장바구니 엔티티 조회

        assertEquals(savedCart.getMember().getId(), member.getId()); // 처음에 저장한 member 엔티티의 id 와 savedCart 에 매핑된 member 엔티티의 id를 비교
    }
}