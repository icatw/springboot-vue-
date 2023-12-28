import {deleteDataRequest, postDataRequest, putDataRequest, putParamsRequest} from "@/utils/axios/request";


/**
 * 查询壁纸列表
 * @param condition 查询条件
 * @returns {*}
 */
export const listWallpaper = (condition) => {
    return postDataRequest('/image/list', condition);
}

/**
 * 新增壁纸信息
 * @param params 请求入参
 * @returns {*}
 */
export const saveWallpaper = (params) => {
    return postDataRequest('/image/save', params);
}

/**
 * 更新壁纸信息
 * @param params 请求入参
 * @returns {*}
 */
export const updateWallpaper = (params) => {
    return postDataRequest('/image/update', params);
}

/**
 * 批量删除壁纸信息
 * @param idList id集合
 * @returns {*}
 */
export const batchDeleteWallpaper = (idList) => {
    return deleteDataRequest('/image/batch/delete', {ids: idList});
}
/**
 * 恢复壁纸信息
 * @param idList id集合
 * @returns {*}
 */
export const recoveryWallpaper = (idList) => {
    return putDataRequest('/image/recovery', {ids: idList});
}
