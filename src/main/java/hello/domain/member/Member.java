package hello.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {
    private Long id; //DB에 저장되는 id

    @NotEmpty(message = "비어 있으면 ㄴㄴㄴ")
    private String loginId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}
