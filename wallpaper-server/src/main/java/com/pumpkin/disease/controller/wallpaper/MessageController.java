package com.pumpkin.disease.controller.wallpaper;

import com.pumpkin.disease.annotation.AnonymousAccess;
import com.pumpkin.disease.annotation.NormalOperateLog;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.wallpaper.Message;
import com.pumpkin.disease.response.security.user.UserResponse;
import com.pumpkin.disease.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.pumpkin.disease.constant.SystemConstant.LogType.*;

/**
 * 留言留言表(Message)表控制层
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Api(tags = "留言表(Message)")
@RestController
@RequestMapping("message")
public class MessageController {

    /**
     * 服务对象
     */
    @Resource
    private MessageService messageService;

    /**
     * 查询留言基础信息列表
     *
     * @return {@link Message} 留言信息
     */
    @ApiOperation(value = "[后台]查询留言基础信息列表")
    @GetMapping("/list/basic")
    public ResponseResult<List<Message>> listBasicType() {
        return ResponseResult.success("查询留言基础信息成功！", messageService.listBasicMessage());
    }

    /**
     * 查询留言列表
     *
     * @param request 留言条件请求入参
     * @return {@link  UserResponse} 留言响应信息
     */
    @ApiOperation(value = "[后台]查询留言列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<Message>> queryTypeList(@RequestBody @Valid BaseRequest request) {
        return ResponseResult.success("查询留言列表信息成功！", messageService.queryMessageList(request));
    }

    /**
     * 新增留言信息
     *
     * @param message 请求入参
     * @return 是否新增成功
     */
    @ApiOperation(value = "[后台]新增留言信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PostMapping("/save")
    public ResponseResult<?> saveUser(@RequestBody @Valid Message message) {
        messageService.saveMessage(message);
        return ResponseResult.success("新增留言成功！");
    }

    /**
     * 更新留言信息
     *
     * @param message 请求入参
     * @return 是否更新成功
     */
    @ApiOperation(value = "[后台]更新留言信息")
    @NormalOperateLog(operateType = UPDATE_OPERATE)
    @PostMapping("/update")
    public ResponseResult<?> updateType(@RequestBody Message message) {
        messageService.updateMessage(message);
        return ResponseResult.success("更新留言信息成功！");
    }

    /**
     * 批量删除留言信息
     *
     * @param idRequest id请求入参
     * @return 是否删除成功
     */
    @ApiOperation(value = "[后台]删除留言信息")
    @NormalOperateLog(operateType = BATCH_DELETE_OPERATE)
    @DeleteMapping("/batch/delete")
    public ResponseResult<?> batchDeleteType(@RequestBody IDRequest<String> idRequest) {
        messageService.batchDeleteMessage(idRequest);
        return ResponseResult.success("删除留言成功！");
    }

    @ApiOperation(value = "[前台] 获取所有留言")
    @GetMapping("/frontend/list/basic")
    @AnonymousAccess
    public ResponseResult<?> list() {
        return ResponseResult.success("查询留言基础信息成功！", messageService.listBasicMessage());
    }

    @ApiOperation(value = "[前台] 新增留言")
    @PostMapping("/frontend/save")
    @AnonymousAccess
    public ResponseResult<?> saveMessage(@RequestBody @Valid Message message) {
        messageService.saveMessage(message);
        return ResponseResult.success("新增留言成功！");
    }
}

