package com.example.uk.repository;

import com.example.uk.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 단수로 찾기
    List<Item> findByItemNm(String itemNm);
    // OR 조건 처리하기
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

}
