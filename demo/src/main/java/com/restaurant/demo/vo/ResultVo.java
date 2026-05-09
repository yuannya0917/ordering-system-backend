package com.restaurant.demo.vo;

import lombok.Data;

@Data
public class ResultVo<T> {
    private Integer code;   // 状态码：200成功，400失败
    private String message; // 提示信息
    private T data;         // 返回的数据

    public static <T> ResultVo<T> success(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> success(String message, T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> error(String message) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
}