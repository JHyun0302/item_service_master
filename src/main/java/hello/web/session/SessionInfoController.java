package hello.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //true로 바꾸면 세션 없을 경우 자동 생셩시킴!
        if (session == null) {
            return "세션이 없습니다.";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name= {}, value={}", name, session.getAttribute(name)));

        log.info("sessionId= {}", session.getId());
        log.info("getMaxInactiveInterval= {}", session.getMaxInactiveInterval());
        log.info("creationTime={}", session.getCreationTime());
        log.info("lastAccessedTime={}", session.getLastAccessedTime());
        log.info("isNew= {}", session.isNew());

        return "세션 출력";
    }
}
