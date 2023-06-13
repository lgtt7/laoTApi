package com.laot.project.provider.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.laot.project.mapper.InterfaceInfoMapper;
import com.laot.project.mapper.UserInterfaceInfoMapper;
import com.laot.project.mapper.UserMapper;
import com.laot.project.model.entity.InterfaceInfo;
import com.laot.project.model.entity.User;
import com.laot.project.model.entity.UserInterfaceInfo;
import com.laot.project.innerInterface.InnerInvoke;
import com.laot.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class InnerInvokeImpl implements InnerInvoke {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    @Autowired
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 根据accessKey查询用户
     * @param accessKey
     * @return
     */
    @Override
    public User isHavingUser(String accessKey) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 判断接口是否存在
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo isHavingInterface(String path, String method) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",path);
        queryWrapper.eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }

    @Override
    public boolean isHavingInvokeCount(Long id, Long interfaceId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",id).eq("interfaceInfoId",interfaceId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum<=0){
            return false;
        }
        return true;
    }

    @Override
    public void invokeCount(Long id, Long interfaceId) {
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("userId",id).eq("interfaceInfoId",interfaceId).setSql("leftNum=leftNum-1,totalNum=totalNum+1");
        userInterfaceInfoService.update(wrapper);
    }
}
