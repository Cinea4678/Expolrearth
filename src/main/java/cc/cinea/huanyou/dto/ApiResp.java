package cc.cinea.huanyou.dto;

import com.spencerwi.either.Either;

/**
 * API响应
 * @param code 响应码
 * @param msg 消息
 * @param data 数据
 *
 * @author cinea
 */
public record ApiResp(int code, String msg, Object data) {
    /**
     * 成功
     */
    public static ApiResp success(Object data) {
        return new ApiResp(10000, "成功", data);
    }

    /**
     * 失败
     */
    public static ApiResp failure(int code, String msg) {
        return new ApiResp(code, msg, null);
    }

    /**
     * 根据Either值来自动构造，默认左侧为成功，右侧为失败。
     */
    public static <T> ApiResp from(Either<T,String> result) {
        return result.fold(
                ApiResp::success,
                (reason) -> ApiResp.failure(4000, reason)
        );
    }
}
