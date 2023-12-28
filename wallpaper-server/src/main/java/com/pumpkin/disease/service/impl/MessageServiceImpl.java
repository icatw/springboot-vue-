package com.pumpkin.disease.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Message;
import com.pumpkin.disease.entity.website.OtherConfig;
import com.pumpkin.disease.enums.website.WebsiteConfigEnum;
import com.pumpkin.disease.mapper.MessageMapper;
import com.pumpkin.disease.service.MessageService;
import com.pumpkin.disease.service.WebsiteConfigService;
import com.pumpkin.disease.utils.ip.IpUtil;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 壁纸分类表(Message)表服务实现类
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Resource
    MessageMapper messageMapper;
    @Resource
    WebsiteConfigService websiteConfigService;
    @Resource
    private HttpServletRequest request;

    @Override
    public List<Message> listBasicMessage() {
        // 查询留言列表
        return baseMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime)
                .eq(Message::getIsReview, true));
    }

    @Override
    public PageResult<Message> queryMessageList(BaseRequest request) {
        //IPage<Message> iPage = baseMapper.queryMessageList(new Page<>(request.getPageNow(), request.getPageSize()), request);
        //return new PageResult<>(iPage.getTotal(), iPage.getRecords());
        return null;
    }

    @Override
    public void saveMessage(Message message) {
        // 判断是否需要审核
        OtherConfig otherConfig = websiteConfigService.getWebsiteConfig(WebsiteConfigEnum.OTHER_MODULE.getWebsiteModuleCode());
        if (otherConfig.getLeaveVerify() == 1) {
            message.setIsReview(0);
        } else {
            message.setIsReview(1);
        }
        // 获取用户ip
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        //User userInfo = UserUtil.getUserInfo();
        //message.setAvatar(userInfo.getAvatar());
        //message.setNickname(userInfo.getNickname());
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        baseMapper.insert(message);
    }

    @Override
    public void updateMessage(Message message) {
        baseMapper.updateById(message);
    }

    @Override
    public void batchDeleteMessage(IDRequest<String> idRequest) {
        if (AssertUtil.isNotNull(idRequest) && !idRequest.getIds().isEmpty()) {
            //删除之前先判断是否存在壁纸
            idRequest.getIds().forEach(id -> {
                baseMapper.deleteById(id);
            });
        }
    }
}

