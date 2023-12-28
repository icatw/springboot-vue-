import {postDataRequest, deleteDataRequest} from "@/utils/axios/request";

const API_PREFIX = '/approval';

/**
 * 提交审核
 * @param moduleId
 * @param moduleType
 * @param remarks 备注
 * @returns {Promise<any>}
 */
export const submit = (moduleId, moduleType, remarks) => {
    const data = {moduleId, moduleType, remarks};
    return postDataRequest(`${API_PREFIX}/submit`, data);
};

/**
 * 撤回
 * @returns {Promise<any>}
 * @param idList
 */
export const withdraw = (idList) => {
    return postDataRequest(`${API_PREFIX}/batch/withdraw`, {ids: idList});
};

/**
 * 驳回
 * @returns {Promise<any>}
 * @param idList
 */
export const batchReject = (idList) => {
    return postDataRequest(`${API_PREFIX}/batch/reject`, {ids: idList});
};

/**
 * 查询审核列表
 * @param condition 查询条件
 * @returns {*}
 */
export const listApproval = (condition) => {
    return postDataRequest(`${API_PREFIX}/list`, condition);
};

/**
 * 批量同意
 * @param idList id集合
 * @returns {*}
 */
export const batchApproval = (idList) => {
    return postDataRequest(`${API_PREFIX}/batch/agree`, {ids: idList});
};
/**
 * 批量拒绝
 * @param idList id集合
 * @returns {*}
 */
export const batchRefuse = (idList) => {
    return postDataRequest(`${API_PREFIX}/batch/refuse`, {ids: idList});
};
