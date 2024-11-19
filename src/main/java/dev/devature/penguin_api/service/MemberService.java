package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Member;
import dev.devature.penguin_api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> getMemberByUserId(String email, Long orgId){
        return memberRepository.findByUserEmailAndOrganizationId(email, orgId);
    }
}
