import {Modal, notification} from "ant-design-vue";


/**
 * 清空表单信息
 * @param clearForm 清空所调用的方法
 */
export const clearFormData = (clearForm) => {
    Modal.confirm({
        title: "警告",
        content: "请确认数据保存成功，离开将不会保存！",
        centered: true,
        okText: "确定",
        cancelText: "取消",
        onOk() {
            clearForm();
        },
    });
};

/**
 * 删除单条数据
 * @param type 删除类型
 * @param fn 删除所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const deleteOneData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要删除这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
};


/**
 * 批量删除数据
 * @param type 删除类型
 * @param fn 删除所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const deleteManyData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要删除这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
/**
 * 批量同意数据
 * @param type 同意类型
 * @param fn 同意所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const approvalManyData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要同意这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
/**
 * 批量驳回数据
 * @param type 驳回类型
 * @param fn 驳回所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const rejectManyData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要驳回这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
/**
 * 批量撤回数据
 * @param type 驳回类型
 * @param fn 驳回所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const withdrawManyData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要撤回这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
/**
 * 批量拒绝数据
 * @param type 拒绝类型
 * @param fn 拒绝所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const refuseManyData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要拒绝这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
/**
 * 恢复数据
 * @param type 恢复类型
 * @param fn 恢复所调用的方法
 * @param idList id集合
 * @param initData 初始化方法
 */
export const recoveryData = (type, fn, idList, initData) => {
    Modal.confirm({
        title: "警告",
        content: `确定要恢复这些${type}信息吗？`,
        centered: true,
        okText: "确定",
        cancelText: "取消",
        async onOk() {
            const res = await fn(idList);
            if (res.success) {
                notification.success({message:res.message});
            }
            initData();
        },
    });
}
