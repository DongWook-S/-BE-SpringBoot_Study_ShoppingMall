package com.example.uk.entity;

import com.example.uk.constant.ItemSellStatus;
import com.example.uk.repository.ItemRepository;
import com.example.uk.repository.MemberRepository;
import com.example.uk.repository.OrderItemRepository;
import com.example.uk.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);   // 아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order 엔티티에 담아줌
        }

        orderRepository.saveAndFlush(order);        // order 엔티티를 저장하면서 강제로 flush 를 호출하여 영속성 컨텍스트에 있는 객체들을 DB 에 반영
        em.clear();                                 // 영속성 컨텍스트의 상태를 초기화 한다.

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);   // 영속성 컨텍스트를 초기화했기 때문에 DB 에서 주문 엔티티를 조회함.

        assertEquals(3, savedOrder.getOrderItems().size()); // itemOrder 엔티티 3개가 실제로 DB 에 저장 됬는지 확인
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();                           // 기존에 만들었던 주문 생성 메소드를 이용하여 주문 데이터를 저장
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        // 영속성 컨텍스트의 상태 초기화 후 order 엔티티에 저장했던 주문 상품 아이디를 이용하여 orderItem 을 DB 에 다시 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("==================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("==================================");
    }
}