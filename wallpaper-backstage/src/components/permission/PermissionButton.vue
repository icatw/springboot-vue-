<template>
  <a-button v-bind="$attrs" v-if="checkPermission()" @click="$emit('click')">
    <slot></slot>
  </a-button>
</template>

<script>
import store from '@/store'

export default {
  name: "PermissionButton",
  props: {
    permissions: {
      type: Array,
      required: true
    },
    // 在这里定义Button组件的所有props
  },
  methods: {
    checkPermission() {
      // 在这里编写检查权限的逻辑
      // 根据权限编码判断用户是否具有其中之一的权限
      const permissions = store.state.userInfo.permissionList; // 获取用户权限列表
      if (permissions.includes("**:**:**")){
        return true
      }
      return this.permissions.some(permission => permissions.includes(permission));
    },
  },
}
</script>

<style scoped>

</style>
