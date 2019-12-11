/**
 * FileName: mgrParamScopeServiceImpl
 * Author:   huang.yj
 * Date:     2019/12/11 14:58
 * Description:
 */
package com.sample.test.service;

import com.github.pagehelper.PageHelper;
import com.sample.test.common.entity.MgrParamScope;
import com.sample.test.common.exception.ServiceException;
import com.sample.test.common.util.SwitchDataSource;
import com.sample.test.dao.mapper.pt.MgrParamScopeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author huang.yj
 * @create 2019/12/11
 * @since 0.0.1
 */
@Service
public class MgrParamScopeServiceImpl implements MgrParamScopeService{
    @Resource
    private MgrParamScopeMapper mgrParamScopeMapper;

    @Override
    @SwitchDataSource(value = "ds2")
    public List<MgrParamScope> selectByParams(Map<String, Object> params) throws ServiceException {
        try {
            PageHelper.startPage(1, 5); //分页，第一页的5条数据，下标从1开始，不是从0开始
            return mgrParamScopeMapper.selectByParams(null);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<MgrParamScope> selectByParams2(Map<String, Object> params) throws ServiceException {
        try {
            PageHelper.startPage(1, 5); //分页，第一页的5条数据，下标从1开始，不是从0开始
            return mgrParamScopeMapper.selectByParams(null);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}