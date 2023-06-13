package com.laot.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laot.project.common.ErrorCode;
import com.laot.project.exception.BusinessException;
import com.laot.project.mapper.InterfaceInfoMapper;
import com.laot.project.model.entity.InterfaceInfo;
import com.laot.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 86131
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-04-06 23:01:18
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

//        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
//        String description = interfaceInfo.getDescription();
//        String url = interfaceInfo.getUrl();
//        String method = interfaceInfo.getMethod();
//        String requestHeader = interfaceInfo.getRequestHeader();
//        String reponseHeader = interfaceInfo.getReponseHeader();
//        Long userId = interfaceInfo.getUserId();
//        Integer status = interfaceInfo.getStatus();
//        Date createTime = interfaceInfo.getCreateTime();
//        Date updateTime = interfaceInfo.getUpdateTime();
//        Integer isDelete = interfaceInfo.getIsDelete();

        if(add){
            if(StringUtils.isAnyBlank(name)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if(StringUtils.isAnyBlank(name) && name.length()<50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"名称过长");
        }



    }
}




