package org.koreait.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils {
    private final HttpSession session;
    private final HttpServletRequest request;
    private final MessageSource messageSource;

    /**
     * 모바일 여부 체크
     * @return
     */
    public boolean isMobile() {
        // 모바일 수동 전환 모드 체크
        String device = (String)session.getAttribute("device");
        if (StringUtils.hasText(device)) {
            return device.equals("MOBILE");
        }

        // 요청 헤더 : User-Agent
        String ua = request.getHeader("User-Agent");

        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return ua.matches(pattern);
    }

    public Map<String, List<String>> getErrorMessages(Errors errors) {//JSON 받을 때는 에러를 직접 가공
        // FieldErrors


        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes()), (p1, p2) -> p1));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }

    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }

    public String getMessage(String code){
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
    }

    /**
     * 모바일, PC 뷰 템플릿 경로 생성
     *
     * @param path
     * @return
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/";

        return prefix + path;
    }

}