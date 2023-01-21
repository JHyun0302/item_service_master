package hello.web;

import hello.domain.member.Member;
import hello.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String homeLoginSpring( //required = false -> 로그인 안 한 사용자도 접속 가능
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
