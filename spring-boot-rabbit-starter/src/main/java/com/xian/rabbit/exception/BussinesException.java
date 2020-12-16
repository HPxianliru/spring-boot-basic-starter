package com.xian.rabbit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-09 09:15
 **/
@Getter
public class BussinesException extends RuntimeException {

    private Integer status = UNAUTHORIZED.value();

    public BussinesException(String msg) {
        super(msg);
    }

    public BussinesException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
    public BussinesException(HttpStatus status) {
        super(status.getReasonPhrase());
        this.status = status.value();
    }
}
