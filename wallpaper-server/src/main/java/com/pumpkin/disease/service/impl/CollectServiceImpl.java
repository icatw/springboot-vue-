package com.pumpkin.disease.service.impl;

import cn.hutool.core.text.StrJoiner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkin.disease.entity.User;
import com.pumpkin.disease.entity.wallpaper.Collect;
import com.pumpkin.disease.entity.wallpaper.CollectVo;
import com.pumpkin.disease.mapper.CollectMapper;
import com.pumpkin.disease.service.CollectService;
import com.pumpkin.disease.utils.redis.RedisCache;
import com.pumpkin.disease.utils.user.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 收藏表(Collect)表服务实现类
 *
 * @author icatw
 * @since 2022-10-16 01:57:07
 */
@Service("collectService")
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Resource
    RedisCache redisCache;

    @Override
    public IPage<CollectVo> pageList(Page<CollectVo> page, String userId) {
        return this.baseMapper.pageListByUserId(page, userId);
    }

    @Override
    public void love(int imageId) {
        User userInfo = UserUtil.getUserInfo();
        Collect collect = this.getOne(new QueryWrapper<Collect>()
                .eq("user_id", userInfo.getId())
                .eq("image_id", imageId));

        collect = Optional.ofNullable(collect)
                .map(c -> {
                    c.setStatus(c.getStatus() == 1 ? 0 : 1);
                    return c;
                })
                .orElseGet(() -> {
                    Collect userCollect = new Collect();
                    userCollect.setUserId(userInfo.getId());
                    userCollect.setImageId(imageId);
                    userCollect.setStatus(1);
                    return userCollect;
                });
        StrJoiner strJoiner = new StrJoiner(":");
        strJoiner.setPrefix("user:");
        strJoiner.append(userInfo.getId()).append("image").append("page");
        redisCache.deleteByPrefix(strJoiner.toString());
        this.saveOrUpdate(collect);
    }
}

