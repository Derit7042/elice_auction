package elice.eliceauction.domain.member.service;

import elice.eliceauction.domain.member.dto.MemberInfoDto;
import elice.eliceauction.domain.member.dto.MemberSignUpDto;
import elice.eliceauction.domain.member.dto.MemberUpdateDto;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.exception.MemberException;
import elice.eliceauction.domain.member.exception.MemberExceptionType;
import elice.eliceauction.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.toEntity();
        member.addUserAuthority();  //USER 권한 부여


        member.encodePassword(passwordEncoder);  // 비밀번호 암호화

        if(memberRepository.findByUsername(member.getUsername()).isPresent()){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        memberUpdateDto.name().ifPresent(member::updateName);
    }


    @Override
    public void updatePassword(String checkPassword, String toBePassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder, toBePassword);
    }


    @Override
    public void withdraw(String checkPassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }

    @Override
    public Member findMemberById(Long id) throws Exception {
        // 주어진 ID로 회원 정보를 조회. 만약 해당 회원이 존재하지 않는 경우, 예외를 발생시킨다.
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
    }



    @Override
    public MemberInfoDto getInfo(Long id) throws Exception {
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberInfoDto(findMember);
    }

    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberInfoDto(findMember);
    }
}