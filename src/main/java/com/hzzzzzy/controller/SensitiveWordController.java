package com.hzzzzzy.controller;

import cn.hutool.core.collection.CollUtil;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.entity.SensitiveWord;
import com.hzzzzzy.sensitive.ACFilter;
import com.hzzzzzy.service.SensitiveWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzzzzzy
 * @date 2025/2/26
 * @description
 */
@Api(value = "敏感词管理", tags = "敏感词管理")
@RestController
@CrossOrigin
@RequestMapping("/sensitive")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService wordService;
    @Autowired
    private ACFilter acFilter;

    @ApiOperation(value = "导入敏感词", tags = "敏感词管理")
    @PostMapping("/import")
    public Result importSensitiveWord(
            @RequestPart
            MultipartFile file
    ) throws IOException {
        // 判断文件后缀为txt
        if (!file.getOriginalFilename().endsWith(".txt")) {
            return new Result<List<String>>().success(false).message("文件格式错误，请上传txt文件");
        }
        List<String> wordList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            wordList.add(line);
        }
        if (CollUtil.isEmpty(wordList)) {
            return new Result<List<String>>().success(false).message("文件解析为空");
        }
        List<SensitiveWord> list = wordList.stream().map(item -> {
            SensitiveWord word = new SensitiveWord();
            word.setWord(item);
            return word;
        }).collect(Collectors.toList());
        // 插入数据库
        wordService.insertBatch(list);
        // 构建ACTrie
        List<String> collect = wordService.list().stream().map(item -> item.getWord()).collect(Collectors.toList());
        acFilter.loadWord(collect);
        return new Result<List<String>>().success().message("导入完成");
    }

    @ApiOperation("下载敏感词导入模板")
    @GetMapping(value = "/downloadTemplate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate(HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("template/SpamWords_V1.0.0.txt");
        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=SpamWords.txt");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(new Result<>().error(BusinessFailCode.DATA_FETCH_ERROR).message("文件下载失败"));
        }
    }

    @ApiOperation("测试敏感词")
    @GetMapping("test")
    public Result test(
            @RequestParam("value")
            String value
    ){
        String filter = acFilter.filter(value);
        return new Result<>().success().message("测试成功").data(filter);
    }
}
