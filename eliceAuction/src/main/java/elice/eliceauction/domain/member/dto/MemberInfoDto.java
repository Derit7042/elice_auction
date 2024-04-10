package elice.eliceauction.domain.member.dto;

import elice.eliceauction.domain.member.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberInfoDto {

    private final String name;
    private final String username;


    @Builder
    public MemberInfoDto(Member member) {
        this.name = member.getName();
        this.username = member.getUsername();
    }
}
