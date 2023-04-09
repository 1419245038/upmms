package com.gd.upmms.controller;

import com.gd.upmms.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${upmms.path.img}")
    private String imgPath;

    @Value("${upmms.path.file}")
    private String filePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //文件上传路径
        File dir=new File(imgPath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //文件后缀名
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid作为新文件名
        UUID uuid = UUID.randomUUID();
        //新文件名
        String newFileName=uuid+suffixName;

        try {
            String  canonicalPath = dir.getCanonicalPath();//获取真实路径
            String uploadPath=canonicalPath+"/"+newFileName;
            log.info("文件上传路径:{}",uploadPath);
            file.transferTo(new File(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(newFileName);
    }

    @PostMapping("/upload/file")
    public R<String> uploadFile(MultipartFile file){

        //文件上传路径
        File dir=new File(filePath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //文件后缀名
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid作为新文件名
        UUID uuid = UUID.randomUUID();
        //新文件名
        String newFileName=uuid+suffixName;

        try {
            String  canonicalPath = dir.getCanonicalPath();//获取真实路径
            String uploadPath=canonicalPath+"/"+newFileName;
            log.info("文件上传路径:{}",uploadPath);
            file.transferTo(new File(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(newFileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(imgPath +"/"+name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/download/file")
    public void downloadFile(String name, String userInfo , HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(filePath +"/"+name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            String fileName=userInfo+"的入党申请书"+name.substring(name.lastIndexOf("."));

            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"UTF-8"));

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
