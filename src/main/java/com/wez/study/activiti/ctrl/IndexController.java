package com.wez.study.activiti.ctrl;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/index")
public class IndexController {
    
//    @Autowired
//    private RuntimeService runtimeService;
    
    
    @GetMapping(value="/index")
    public Map<String, Object> toIndex() {
//        runtimeService.getProcessInstanceEvents("123");
        return null;
    }

}
