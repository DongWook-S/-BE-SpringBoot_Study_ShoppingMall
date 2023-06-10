package com.example.uk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartOrderDto {

    private Long cartItemId;

    // 장바구니에서 여러 개의 상품을 주문하므로 CartOrderDto 클래가 자기 자신을 List 로 가지고 있도록 만듬
    private List<CartOrderDto> cartOrderDtoList;
}
