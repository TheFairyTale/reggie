package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载控制器类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 文件上传接口
     *
     */
    @PostMapping("/upload")
    public Result uploadFiles(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("传入的内容是空的");
        }

        long size = file.getSize();
        System.out.println("Size: " + size);
        System.out.println(file.toString());
        return Result.error("debugging, uploaded file received, size: " + size);
    }
}
