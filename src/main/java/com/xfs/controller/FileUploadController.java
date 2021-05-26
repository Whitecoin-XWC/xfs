package com.xfs.controller;

import com.xfs.commons.vo.PageResultVO;
import com.xfs.commons.vo.ResultVO;
import com.xfs.controller.vo.SelectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("fileUpload")
public class FileUploadController {

    /**
     * 保存文件
     * @return
     */
    public ResultVO save(){
        return null;
    }

    /**
     * 获取文件流
     * @return
     */
    public byte[] getFile(){
        return null;
    }

    /**
     * 获取一个文件
     * @return
     */
    public ResultVO getFileDetail(){
        return null;
    }

    /**
     * 查询当前用户的所有文件
     * @return
     */
    public PageResultVO selectFiles(SelectVO selectVO){
        return null;
    }
}
