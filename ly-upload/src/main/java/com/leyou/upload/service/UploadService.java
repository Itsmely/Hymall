package com.leyou.upload.service;

import com.leyou.upload.web.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    //支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png","image/jpeg");

    /**
     * 1. 校验文件大小
     * 2. 校验文件的媒体类型
     * 3. 校验文件的内容
     * @param file
     * @return
     */

    public String upload(MultipartFile file) {
        try{
            //判断文件类型是否合法
            String type = file.getContentType();
            if(!suffixes.contains(type)){
                logger.info("图片类型不合法");
                return null;
            }
            //校验图片内容是否合法
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                logger.info("图片内容不合法");
                return null;
            }

            File dir = new File("C:\\Users\\lee\\Desktop\\img");
            if(!dir.exists()){
                dir.mkdirs();
            }

            file.transferTo(new File(dir,file.getOriginalFilename()));
            String url = "http://image.leyou.com/"+file.getOriginalFilename();

            return url;

        }catch (Exception e){
            return null;
        }

    }
}
