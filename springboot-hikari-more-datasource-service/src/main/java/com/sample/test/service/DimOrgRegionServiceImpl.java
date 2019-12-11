/**
 * FileName: DimOrgRegionServiceImpl
 * Author:   huang.yj
 * Date:     2019/12/11 15:36
 * Description:
 */
package com.sample.test.service;

import com.sample.test.common.entity.DimOrgRegion;
import com.sample.test.common.exception.ServiceException;
import com.sample.test.common.util.SwitchDataSource;
import com.sample.test.dao.mapper.bi.DimOrgRegionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈〉
 *
 * @author huang.yj
 * @create 2019/12/11
 * @since 0.0.1
 */
@Service
public class DimOrgRegionServiceImpl implements DimOrgRegionService {

    @Resource
    private DimOrgRegionMapper dimOrgRegionMapper;

    @Override
    @SwitchDataSource  // 没有value属性值，故使用默认值master
    public List<DimOrgRegion> getQueryRegion() throws ServiceException {
        try {
            return dimOrgRegionMapper.getQueryRegion();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}