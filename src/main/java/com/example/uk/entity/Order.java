package com.example.uk.entity;

import com.example.uk.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders") // 정렬할 때 사용하는 'order' 키워드가 있기 때문에 orders 로 지정
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;    // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문상태

    /*
    *   CascadeType.ALL : 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이
    *   orphanRemoval = true : 고아객체(부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 고아객체) 제거
    */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
}
