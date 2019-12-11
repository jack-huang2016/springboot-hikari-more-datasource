package com.sample.test.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MgrParamScope implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -803904122948458856L;

	/**
     * API ID
     */
    private Integer apiId;

    /**
     * TOKEN
     */
    private String token;

    /**
     * 参数名
     */
    private String paramKey;

    /**
     * 参数值范围
     */
    private String value;

}