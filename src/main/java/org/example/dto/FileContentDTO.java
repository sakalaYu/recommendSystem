package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：wzm
 * @Package：org.example.dto
 * @Project：tuijianserver
 * @name：FileContentDTO
 * @Date：2024/3/4 8:30
 * @Filename：FileContentDTO
 */
@Data
public class FileContentDTO {
    private Integer id;  //b不用管
    private String file_name;//标题
    private String upload; //上传时间
    private String title; //内容
    private String file_path;//文件路径
    private String file_image;//图片路径
    private Integer type; //类型
    private String price; //价格
}
