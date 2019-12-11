package com.sample.test.service;

import com.sample.test.common.entity.DimOrgRegion;
import com.sample.test.common.exception.ServiceException;
import java.util.List;

public interface DimOrgRegionService {
    public List<DimOrgRegion> getQueryRegion() throws ServiceException;
}
