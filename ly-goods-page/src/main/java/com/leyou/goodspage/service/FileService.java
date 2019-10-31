package com.leyou.goodspage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.leyou.common.utils.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class FileService {
    @Autowired
    private PageService pageService;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${ly.thymeleaf.destPath}")
    private String destPath;

    /**
     * 创建html页面
     */
    public void createHtml(Long id) throws Exception {
        //创建上下文
        Context context = new Context();
        //载入数据
        context.setVariables(pageService.loadModel(id));

        //生成临时html
        File temp = new File(id+".html");
        //创建目标文件
        File dest = createPath(id);
        //备份原页面文件
        File bak = new File(id+"_bak.html");
        //生成静态html
        try(PrintWriter writer = new PrintWriter(temp,"UTF-8")){
            //生成静态文件
            templateEngine.process("item",context,writer);

            if(dest.exists()){
                //如果目标文件已存在，备份dest
                dest.renameTo(bak);
            }
            //将新页面覆盖到旧页面
            FileCopyUtils.copy(temp,dest);
            //成功后将旧页面删除
            bak.delete();

        }catch (IOException e){
            //失败后，将bak恢复
            bak.renameTo(dest);
            throw new Exception(e);
        }finally {
            //删除临时页面
            if(temp.exists()){
                temp.delete();
            }
        }
    }

    private File createPath(Long id) {
        if(id == null){
            return null;
        }
        File dest = new File(destPath);
        if(!dest.exists()){
            dest.mkdirs();
        }
        return new File(destPath,id+".html");
    }

    //判断是否存在id.html
    public boolean exists(Long id) {
        return this.createPath(id).exists();
    }

    public void syncCreateHtml(Long id) {
        ThreadUtils.execute(() ->{
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
