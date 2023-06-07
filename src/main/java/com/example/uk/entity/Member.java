package com.example.uk.entity;

import com.example.uk.constant.Role;
import com.example.uk.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    /*
     * 동일한 값이 DB 에 들어올 수 없게 unique 속성 지정
     */
    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    /*
     * JAVA 의 ENUM 타입을 엔티티의 속성으로 지정
     * ENUM 의 순서가 바뀔 경우 문제가 발생할 수 있으므로 EnumType.STRING 옵션을 사용해서 String 으로 저장
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        // 패스워드를 암호화로 변경
        String password = passwordEncoder.encode(memberFormDto.getPassword());

        member.setPassword(password);
        member.setRole(Role.ADMIN);

        return member;
    }
}
