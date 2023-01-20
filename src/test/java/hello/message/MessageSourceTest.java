package hello.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
public class MessageSourceTest {
    @Autowired
    MessageSource ms; //messages.pro~, messages_en.pro~ 등록

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        /*try {
            String result = ms.getMessage("no_code", null, null);
        } catch (NoSuchMessageException e) {
            log.info("NoSuchMessageException");
        }*/
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "메시지가 없으면 기본 메시지", null);
        assertThat(result).isEqualTo("메시지가 없으면 기본 메시지");
    }

    @Test
    void argumentMessage() {
        String result = ms.getMessage("hello.name", new Object[]{"Spring!", "Boot!"}, null);
        assertThat(result).isEqualTo("안녕 Spring! Boot!");
    }

    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREAN)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello.name", new Object[]{"꼬마 돼지", "혁준"}, null)).isEqualTo("안녕 꼬마 돼지 혁준");
    }

    @Test
    void enLang() {
        assertThat(ms.getMessage("hello.name", new Object[]{"혁준 남~"}, Locale.ENGLISH)).isEqualTo("hello 혁준 남~");
    }

}
