package com.example.uk.service;

import com.example.uk.dto.OrderDto;
import com.example.uk.dto.OrderHistDto;
import com.example.uk.dto.OrderItemDto;
import com.example.uk.entity.*;
import com.example.uk.repository.ItemImgRepository;
import com.example.uk.repository.ItemRepository;
import com.example.uk.repository.MemberRepository;
import com.example.uk.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);   // 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email);                // 유저의 주문 총 개수를 구함

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {    // 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO 를 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");  // 주문한 상품의 대표 이미지를 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount); // 페이지 구현 객체를 생성하여 반환
    }
}
