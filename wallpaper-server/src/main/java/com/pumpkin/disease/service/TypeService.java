package com.pumpkin.disease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Type;
import com.pumpkin.disease.request.security.wallpaper.TypeConditionRequest;

import java.util.List;

/**
 * 壁纸分类表(Type)表服务接口
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
public interface TypeService extends IService<Type> {
    /**
     * 列出基本类型
     *
     * @return {@link List}<{@link Type}>
     */
    List<Type> listBasicType();

    /**
     * 查询类型列表
     *
     * @param request 要求
     * @return {@link PageResult}<{@link Type}>
     */
    PageResult<Type> queryTypeList(TypeConditionRequest request);

    /**
     * 保存类型
     *
     * @param type 类型
     */
    void saveType(Type type);

    /**
     * 更新类型
     *
     * @param type 类型
     */
    void updateType(Type type);

    /**
     * 批量删除类型
     *
     * @param idRequest id请求
     */
    void batchDeleteType(IDRequest<String> idRequest);
}

