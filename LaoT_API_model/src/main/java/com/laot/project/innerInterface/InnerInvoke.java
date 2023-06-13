package com.laot.project.innerInterface;

import com.laot.project.model.entity.InterfaceInfo;
import com.laot.project.model.entity.User;

public interface InnerInvoke {
    User isHavingUser(String accessKey);

    InterfaceInfo isHavingInterface(String path, String method);

    boolean isHavingInvokeCount(Long id,Long interfaceId);

    void invokeCount(Long id,Long interfaceId);
}
