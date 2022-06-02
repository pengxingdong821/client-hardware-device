package com.fontre.clienthardwaredevice.handler;

import com.fontre.clienthardwaredevice.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	  /**
     * 处理所有不可知的异常
     */
//    @ExceptionHandler(Throwable.class)
//    public ResponseEntity<ApiError> handleException(Throwable e){
//        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
//        return buildResponseEntity(ApiError.error("UnknownException"));
//    }
//    
    /**
     * 处理自定义异常
     */
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ApiError> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getStatus(),e.getMessage()));
	}


    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if(msg.equals(message)){
            message = str[1] + ":" + message;
        }
        return buildResponseEntity(ApiError.error("Data validation exception"));
    }

    /**
     * 处理其它异常
     */
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiError> exceptionHandler(Exception e) {
//    	log.error(ThrowableUtil.getStackTrace(e));
//    	return buildResponseEntity(ApiError.error("Exception"));
//    }
    
    /**
     * 处理所有的异常和Throwable类
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class,Throwable.class})
    @ResponseBody
    public ResponseEntity<ApiError> exceptionHandler(Exception e) {
    	log.error(e.getMessage());
    	return buildResponseEntity(ApiError.error("Exception"));
    }
//    public Map<String,Object> testExceptionHandler1(Exception e){
//        System.out.println("进来了："+e);
//        Map<String, Object> map = new HashMap<>();
//        map.put("code",400);
//        map.put("msg","出错啦");
//        return map;
//
//    }
    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
