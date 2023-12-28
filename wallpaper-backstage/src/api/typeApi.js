import {deleteDataRequest, getRequest, postDataRequest} from "@/utils/axios/request";


/**
 * 查询分类列表
 * @param condition 查询条件
 * @returns {*}
 */
export const listType = (condition) => {
    return postDataRequest('/type/list', condition);
}
/**
 * 获取所有分类
 */
export const listTypeBasic = (condition) => {
    return getRequest('/type/list/basic');
}

/**
 * 新增分类信息
 * @param params 请求入参
 * @returns {*}
 */
export const saveType = (params) => {
    return postDataRequest('/type/save', params);
}

/**
 * 更新分类信息
 * @param params 请求入参
 * @returns {*}
 */
export const updateType = (params) => {
    return postDataRequest('/type/update', params);
}

/**
 * 批量删除分类信息
 * @param idList id集合
 * @returns {*}
 */
export const batchDeleteType = (idList) => {
    return deleteDataRequest('/type/batch/delete', {ids: idList});
}

/**
 * 重置分类面
 * @param id 分类id
 * @param password 分类密码
 * @returns {*}
 */
export const resetPassword = (id, password) => {
    return postDataRequest(`/type/reset/password/${id}`, {"password": password});
}
