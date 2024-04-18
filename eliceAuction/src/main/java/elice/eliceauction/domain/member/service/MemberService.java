package elice.eliceauction.domain.member.service;

import elice.eliceauction.domain.member.dto.MemberInfoDto;
import elice.eliceauction.domain.member.dto.MemberSignUpDto;
import elice.eliceauction.domain.member.dto.MemberUpdateDto;
import elice.eliceauction.domain.member.entity.Member;
import elice.eliceauction.domain.member.exception.MemberException;

import java.util.Optional;

public interface MemberService {

    /**
     * 회원가입
     * 정보수정
     * 회원탈퇴
     * 정보조회
     */

    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;

    void update(MemberUpdateDto memberUpdateDto) throws Exception;

    void updatePassword(String checkPassword, String toBePassword) throws Exception;

    void withdraw(String checkPassword) throws Exception;

    Member findMemberById(Long id) throws Exception;
    MemberInfoDto getInfo(Long id) throws Exception;

    MemberInfoDto getMyInfo() throws Exception;


    Member findMemberByUsername(String username) throws Exception;
}