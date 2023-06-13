package com.laot.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laot.project.model.entity.InterfaceInfo;

/**
* @author 86131
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-04-06 23:01:18
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
