package org.koreait.main.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final Utils utils;


    /**
     * 메인 페이지 출력
     *
     * @return
     */
    @GetMapping("/")
    public String index() {

        return utils.tpl("main/index");
    }
}
