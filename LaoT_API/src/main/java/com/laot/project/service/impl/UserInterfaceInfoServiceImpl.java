package com.laot.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laot.project.common.ErrorCode;
import com.laot.project.exception.BusinessException;
import com.laot.project.mapper.UserInterfaceInfoMapper;
import com.laot.project.model.entity.UserInterfaceInfo;
import com.laot.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86131
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-05-28 22:13:52
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        if(add){
            if(userInterfaceInfo.getInterfaceInfoId()<=0 || userInterfaceInfo.getUserId()<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        if(userInterfaceInfo.getLeftNum()<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数小于0");
        }



    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //判断
        if(interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
        }

        boolean isOk = this.update().setSql("leftNum=leftNum-1,totalNum=totalNum+1")
                .eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId).update();


        return isOk;
    }
}




