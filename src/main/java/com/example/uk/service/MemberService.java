package com.example.uk.service;

import com.example.uk.entity.Member;
import com.example.uk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    /*
     * Transactional : 로직을 처리하다가 에러가 발생하면 변경된 데이터를 로직을 수행하기 이전 상태로 롤백 해줌
     * RequiredArgsConstructor : final 이나 @NonNull 이 붙은 필드에 생성자를 생성
     */

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
