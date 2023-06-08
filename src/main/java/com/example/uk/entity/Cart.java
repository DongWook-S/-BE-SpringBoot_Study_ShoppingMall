package com.example.uk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    /*
     *  @OneToOne   :   1:1
     *  @OneToMany  :   1:N
     *  @ManyToOne  :   N:1
     *  @ManyToMany :   N:M
     */

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키 지정
    private Member member;

}
