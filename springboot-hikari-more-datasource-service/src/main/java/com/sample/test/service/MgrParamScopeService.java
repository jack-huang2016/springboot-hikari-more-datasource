package com.sample.test.service;

import com.sample.test.common.entity.MgrParamScope;
import com.sample.test.common.exception.ServiceException;
import java.util.List;
import java.util.Map;

public interface MgrParamScopeService {
    public List<MgrParamScope> selectByParams(Map<String, Object> params) throws ServiceException;

    public List<MgrParamScope> selectByParams2(Map<String, Object> params) throws ServiceException;
}
