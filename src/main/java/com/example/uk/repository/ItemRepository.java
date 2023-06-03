package com.example.uk.repository;

import com.example.uk.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /*
     * 단수로 찾기
     */
    List<Item> findByItemNm(String itemNm);

    /*
     * OR 조건 처리하기
     */
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    /*
     * LessThan 조건 처리하기
     */
    List<Item> findByPriceLessThan(Integer price);

    /*
     * @Query 를 이용한 검색 처리하기(JPQL)
     */
    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    /*
     * @Query - nativeQuery 속성 예제
     * 만약 기존의 DB 에서 사용하던 쿼리를 그대로 사용해야 할 대는 @Query 의 nativeQuery 속성을 사용하면 기존 쿼리를 그대로 활용할 수 있다.
     * 하지만 특성 DB 에 종속되는 쿼리문을 사용하기 때문에 DB 에 대해 독립적이라는 장점을 잃어버린다.
     */
    @Query(value = "SELECT * FROM item i WHERE i.item_detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
