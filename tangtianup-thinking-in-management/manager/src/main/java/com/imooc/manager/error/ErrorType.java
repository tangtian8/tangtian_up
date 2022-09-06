package com.imooc.manager.error;

/**
 * 错误类型
 */
public enum ErrorType {
    ID_IS_NULL("F001", "编号不可为空"),
    UNKNOWN_ERROR("999", "未知异常");

    private String code;
    private String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据名称获取错误类型
     *
     * @param name
     * @return
     */
    public static ErrorType getByName(String name) {
        for (ErrorType type : ErrorType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return ErrorType.UNKNOWN_ERROR;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
