package com.sample.test.web.controller;

import com.sample.test.common.entity.DimOrgRegion;
import com.sample.test.common.entity.MgrParamScope;
import com.sample.test.common.exception.ServiceException;
import com.sample.test.dao.config.DataSourceSwitch;
import com.sample.test.service.DimOrgRegionService;
import com.sample.test.service.MgrParamScopeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/testMoreData")
public class HikariMoreDataController {

    @Resource
    private DimOrgRegionService dimOrgRegionService;

    @Resource
    private MgrParamScopeService mgrParamScopeService;

    @RequestMapping("/selectMgrParam1")
    public ResponseEntity<List<MgrParamScope>> selectMgrParam1(){
        List<MgrParamScope> ds3List = null;
        try {
            ds3List = mgrParamScopeService.selectByParams(null);
            return ResponseEntity.ok(ds3List);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping("/selectMgrParam2")
    public ResponseEntity<List<MgrParamScope>> selectMgrParam2(){
        List<MgrParamScope> ds3List = null;
        try {
            DataSourceSwitch.setDataSourceType("ds3");
            ds3List = mgrParamScopeService.selectByParams2(null);
            return ResponseEntity.ok(ds3List);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DataSourceSwitch.clearDataSourceType();
        }
    }

    @RequestMapping("/selectDimOrgRegion")
    public ResponseEntity<List<DimOrgRegion>> selectMgrParam3(){
        List<DimOrgRegion> ds3List = null;
        try {
            ds3List = dimOrgRegionService.getQueryRegion();
            return ResponseEntity.ok(ds3List);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
