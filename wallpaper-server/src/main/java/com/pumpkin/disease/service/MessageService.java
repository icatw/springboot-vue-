package com.pumpkin.disease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Message;

import java.util.List;

/**
 * 壁纸分类表(Message)表服务接口
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
public interface MessageService extends IService<Message> {
    /**
     * 列出基本类型
     *
     * @return {@link List}<{@link Message}>
     */
    List<Message> listBasicMessage();

    /**
     * 查询类型列表
     *
     * @param request 要求
     * @return {@link PageResult}<{@link Message}>
     */
    PageResult<Message> queryMessageList(BaseRequest request);

    /**
     * 保存类型
     *
     * @param message 类型
     */
    void saveMessage(Message message);

    /**
     * 更新类型
     *
     * @param message 类型
     */
    void updateMessage(Message message);

    /**
     * 批量删除类型
     *
     * @param idRequest id请求
     */
    void batchDeleteMessage(IDRequest<String> idRequest);
}

