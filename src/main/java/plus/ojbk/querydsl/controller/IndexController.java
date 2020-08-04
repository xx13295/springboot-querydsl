package plus.ojbk.querydsl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @RequestMapping("/index")
    public Object index(){
        return "hello world!";
    }

}
