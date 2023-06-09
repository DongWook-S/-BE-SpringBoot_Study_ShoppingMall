package com.example.uk.service;

import com.example.uk.constant.ItemSellStatus;
import com.example.uk.constant.OrderStatus;
import com.example.uk.dto.OrderDto;
import com.example.uk.entity.Item;
import com.example.uk.entity.Member;
import com.example.uk.entity.Order;
import com.example.uk.entity.OrderItem;
import com.example.uk.repository.ItemRepository;
import com.example.uk.repository.MemberRepository;
import com.example.uk.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveItem();             // 테스트를 위해서 상품과 회원 데이터를 생성함.
        Member member = saveMember();       // 생성한 상품의 재고는 100개

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail()); // 테스트를 위해서 주문 데이터를 생성. 주문 개수는 총 10개.

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);  // 생성한 주문 엔티티를 조회.
        orderService.cancelOrder(orderId);  // 해당 주문을 취소

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());   // 주문의 상태가 취소 상태라면 테스트 통화
        assertEquals(100, item.getStockNumber());           // 취소 후 상품의 재고가 처음 재고 개수인 100개와 동일하다면 테스트 통과
    }
}