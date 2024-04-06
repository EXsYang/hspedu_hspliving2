package com.hspedu.common.exception;

/**
 * @author yangda
 * @create 2024-03-16-21:50
 * @description: 代码优化-统一状态码
 * 问题分析: 公司写代码会统一规定错误/异常状态码 和 提示信息，我们可以将其抽
 * 取成枚举类，方便管理和扩展
 */
public enum HsplivingCodeEnum {
    // 回顾java基础枚举类的使用
    UNKNOWN_EXCEPTION(40000,"系统未知异常"),
    INVALID_EXCEPTION(40001,"参数校验异常");


    private int code;
    private String msg;


    HsplivingCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
