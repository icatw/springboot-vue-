import store from '@/store'
/**
 * 判断用户是否拥有操作权限
 * 根据传入的权限标识，查看是否存在用户权限标识集合
 * @param perms
 */
export function hasPermission(perms) {
    const permissions = store.state.userInfo.permissionList
    if (permissions.includes("**:**:**")){
        return true
    }
    return perms.some(p => permissions.includes(p));
}

