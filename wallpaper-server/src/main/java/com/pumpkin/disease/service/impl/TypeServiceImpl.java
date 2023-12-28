package com.pumpkin.disease.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.entity.wallpaper.Type;
import com.pumpkin.disease.mapper.ImageMapper;
import com.pumpkin.disease.mapper.TypeMapper;
import com.pumpkin.disease.request.security.wallpaper.TypeConditionRequest;
import com.pumpkin.disease.service.TypeService;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 壁纸分类表(Type)表服务实现类
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Service("typeService")
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Resource
    ImageMapper imageMapper;

    @Override
    public List<Type> listBasicType() {
        return baseMapper.listBasicType();
    }

    @Override
    public PageResult<Type> queryTypeList(TypeConditionRequest request) {
        IPage<Type> iPage = baseMapper.queryTypeList(new Page<>(request.getPageNow(), request.getPageSize()), request);
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public void saveType(Type type) {
        baseMapper.insert(type);
    }

    @Override
    public void updateType(Type type) {
        baseMapper.updateById(type);
    }

    @Override
    public void batchDeleteType(IDRequest<String> idRequest) {
        if (AssertUtil.isNotNull(idRequest) && idRequest.getIds().size() > 0) {
            LambdaQueryChainWrapper<Image> chainWrapper = new LambdaQueryChainWrapper<Image>(imageMapper);
            //删除之前先判断是否存在壁纸
            idRequest.getIds().forEach(id -> {
                Long count = chainWrapper.eq(Image::getIsDeleted, 0).eq(Image::getTypeId, id).count();
                AssertUtil.assertIsNotPass(count > 0, "当前分类下存在壁纸，禁止删除！");
                baseMapper.deleteById(id);
            });
        }
    }
}

