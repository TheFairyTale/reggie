package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传下载控制器类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 获取配置文件中的文件存储目录
     *
     */
    @Value("${reggie.file-path}")
    private String filePath;

    /**
     * 文件上传接口
     *
     * @param file 上传来的文件 这个文件是一个临时文件，如果不转存到其他地方，则本次请求完成后该临时文件会被删除
     */
    @PostMapping("/upload")
    public Result<String> uploadFiles(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("传入的内容是空的");
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }

        // 1. 获取原始文件名，通过原始文件名获取文件后缀
        String[] split = file.getOriginalFilename().split("\\.");
        int i = (split.length) - 1;
        String suffix = split[i];

        // 2. 为了防止上传的文件名与其他文件的名称相同， 通过UUID 重新生成一个文件名称
        long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
        String newFileName = mostSignificantBits + "." + suffix;

        // 3. 创建文件存放目录
        // 创建一个目录对象
        File baseDirectory = new File(filePath);
        // 判断该目录是否存在, 不存在则创建
        if (!baseDirectory.exists()) {
            log.warn(baseDirectory.mkdirs() ? "Directories path "+ baseDirectory.getPath() +" created" : "Directories create failed.");
        }

        // 4. 将上传的临时文件转存至指定位置
        try {
            file.transferTo(new File(baseDirectory + "/" + newFileName));
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }
        log.info("Before return: => " + newFileName);
        return Result.success(newFileName);
    }

    /**
     * 获取指定名称的文件, 并通过HttpServletResponse 返回
     *
     * @param name
     * @param httpServletResponse
     * @return
     * @throws IOException
     */
    @GetMapping("/download")
    public Result downloadFile(@RequestParam String name, HttpServletResponse httpServletResponse) throws IOException {
        if (name == null || "null".equals(name)) {
            return Result.error("无法下载指定的文件，文件名为空");
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }

        log.info("Before find file: => " + name);

        // 读指定的文件
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(filePath + name)));
        // 获取Http响应输出流, 通过输出流将文件返回给浏览器
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        // 缓冲区
        byte[] byteBuffer = new byte[1024];
        // bufferedInputStream.read(用于存放读取了的数据的byte数组) 返回单次已读的字节数
        while (bufferedInputStream.read(byteBuffer) != -1) {
            //
            outputStream.write(byteBuffer);
            outputStream.flush();
        }

        outputStream.close();
        bufferedInputStream.close();

        return Result.success("成功");
    }
}
