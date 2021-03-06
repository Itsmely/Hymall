package com.leyou.goodspage.controller;

import com.leyou.goodspage.service.FileService;
import com.leyou.goodspage.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/item")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private FileService fileService;

    @GetMapping("/{id}.html")
    public String toItemPage(Model model,@PathVariable("id") Long id){

        //加载所需数据
        Map<String, Object> modelMap = pageService.loadModel(id);
        //放入模型
        model.addAllAttributes(modelMap);

        //判断是否需要生成新的页面
        if(!this.fileService.exists(id)){
            this.fileService.syncCreateHtml(id);
        }

        return "item";
    }

}
