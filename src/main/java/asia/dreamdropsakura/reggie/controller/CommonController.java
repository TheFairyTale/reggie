package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
     * 文件上传接口
     *
     * @param file 上传来的文件 这个文件是一个临时文件，如果不转存到其他地方，则本次请求完成后该临时文件会被删除
     */
    @PostMapping("/upload")
    public Result uploadFiles(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("传入的内容是空的");
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
        File baseDirectory = new File(new File("").getAbsolutePath() + "/temp");
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
        return Result.success("图片上传成功");
    }
}
