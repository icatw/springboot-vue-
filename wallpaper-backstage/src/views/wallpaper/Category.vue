<template>
  <a-card class="main-container animate__animated animate__fadeIn" style="border-radius: 5px">
    <!-- 页面标题 -->
    <page-title/>
    <!-- 页面主要内容 -->
    <div class="page-content">
      <!-- 搜索部分 -->
      <div class="search-container animate__animated animate__flipInX" v-if="!hideSearch">
        <!-- 搜索条件 -->
        <div class="search-box">
          <!-- 分类名 -->
          <div class="search-item">
            <span class="label-title">分类名</span>
            <a-input class="label-component"
                     v-model="condition.typeName"
                     allowClear
                     placeholder="请输入分类名..."/>
          </div>
          <!-- 创建时间 -->
          <div class="search-item">
            <span class="label-title">创建时间</span>
            <a-range-picker
                v-model="timeValue"
                :locale="locale"
                @change="dateChange"
                class="label-component"/>
          </div>
        </div>
        <!-- 搜索按钮 -->
        <div class="search-box">
          <!-- 操作按钮 -->
          <div class="search-item">
            <a-button style="margin-right: 10px"
                      size="small"
                      type="primary"
                      icon="search"
                      @click="initData">
              查询
            </a-button>
            <a-button icon="reload"
                      size="small"
                      @click="resetCondition">
              重置
            </a-button>
          </div>
        </div>
      </div>
      <a-divider style="margin: 15px 0"></a-divider>
      <!-- 操作部分 -->
      <div class="operate-btn-container">
        <div class="operate-left-box">
          <a-button class="operate-left-item"
                    size="small"
                    type="primary"
                    icon="plus"
                    @click="toSaveOrUpdate(null)">
            新增
          </a-button>
          <a-button class="operate-left-item"
                    :disabled="deleteList.length === 0"
                    size="small"
                    @click="deleteMany"
                    type="danger"
                    icon="delete">
            批量删除
          </a-button>
        </div>
        <div class="operate-right-box">
          <a-tooltip placement="top">
            <template slot="title">
              <span>{{ hideSearch ? '展示搜索' : '隐藏搜索' }}</span>
            </template>
            <a-button

                style="margin-right: 10px"
                size="small"
                @click="hideSearch = !hideSearch"
                :icon="hideSearch ? 'zoom-in':'zoom-out'"/>
          </a-tooltip>

          <a-tooltip placement="top">
            <template slot="title">
              <span>刷新</span>
            </template>
            <a-button
                @click="initData"
                style="margin-right: 10px"
                size="small"
                icon="sync"></a-button>
          </a-tooltip>
        </div>
      </div>
      <!-- 表格数据 -->
      <a-spin
          :spinning="loading"
          tip="正在努力拉取数据...">
        <!--表格内容-->
        <a-table
            :scroll="{x: 1300}"
            :rowSelection="rowSelectList"
            size="small"
            bordered
            :data-source="typeList"
            :rowKey="(type) => type.id"
            :pagination="false">
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="分类名"
              data-index="typeName">
            <template slot-scope="typeName">
              <a-tooltip>
                <template slot="title">
                  {{ typeName }}
                </template>
                {{ typeName }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="描述"
              data-index="description">
            <template slot-scope="description">
              <a-input type="textarea" readOnly :value="description" :auto-size="{ minRows: 2, maxRows: 5 }"/>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="7%"
              title="状态"
              data-index="status">
            <template slot-scope="status">
              <a-tag v-if="status === 0" size="small" color="#f50">禁用</a-tag>
              <a-tag v-else-if="status === 1" size="small" color="#2db7f5">正常</a-tag>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="壁纸数量"
              data-index="imageNum">
            <template slot-scope="imageNum">
              <a-tooltip>
                <template slot="title">
                  {{ imageNum }}
                </template>
                {{ imageNum }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              title="创建时间"
              data-index="createTime">
            <template slot-scope="createTime">
              <a-icon type="bell"/>
              {{ createTime }}
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              title="修改时间"
              data-index="updateTime">
            <template slot-scope="updateTime">
              <a-icon type="bell"/>
              {{ updateTime }}
            </template>
          </a-table-column>
          <a-table-column
              width="280px"
              align="center"
              fixed="right"
              title="操作">
            <template slot-scope="scope"
                      v-if="scope.roleName !== '超级管理员'">
              <a-button
                  style="margin-right: 10px"
                  size="small"
                  icon="edit"
                  @click="toSaveOrUpdate(scope)">
                编辑
              </a-button>
              <a-button
                  size="small"
                  type="danger"
                  icon="delete"
                  @click="deleteOne(scope.id)">
                删除
              </a-button>
            </template>
          </a-table-column>
        </a-table>
      </a-spin>
      <!-- 分页 -->
      <a-pagination
          class="page-center"
          :total="total"
          show-size-changer
          :defaultPageSize="8"
          :pageSizeOptions="['8', '16', '24', '32']"
          :show-total="(total) => `共有 ${total} 个分类`"
          @change="currentChange"
          @showSizeChange="sizeChange">
        <template slot="buildOptionText" slot-scope="props">
          <span v-if="props.value !== '50'">{{ props.value }}条/页</span>
        </template>
      </a-pagination>
    </div>

    <!-- 抽屉弹窗 -->
    <a-drawer
        :title="drawerIsSave ? '新增分类':'修改分类信息'"
        :width="680"
        :visible="saveOrUpdateDrawerShow"
        @close="cancelSaveOrUpdate">
      <a-form-model
          ref="formData"
          :model="formData"
          :rules="rules"
          layout="vertical">

        <!-- 分类名-->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="分类名：" prop="typeName">
              <a-input v-model="formData.typeName" placeholder="分类名..."/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="状态" prop="status">
              <a-radio-group v-model="formData.status">
                <a-radio :value="1">
                  在用
                </a-radio>
                <a-radio :value="0">
                  禁用
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-form-model-item label="描述">
            <a-input v-model="formData.description" type="textarea" :rows="8" placeholder="请输入描述"/>
          </a-form-model-item>
        </a-row>
      </a-form-model>

      <div class="drawer-footer-btn">
        <a-button style="margin-right: 10px" @click="cancelSaveOrUpdate">取消</a-button>
        <a-button type="primary" @click="doSaveOrUpdate">确定</a-button>
      </div>
    </a-drawer>
  </a-card>
</template>

<script>
import PageTitle from "@/components/layout/page/PageTitle";
import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";
import {listType, saveType, updateType, batchDeleteType} from "@/api/typeApi";
import {listTypeBasic} from "@/api/typeApi";
import {uploadPicture} from "@/api/uploadApi";
import {mapState} from "vuex";
import {checkResult} from "@/utils/result/resultUtil";
import {clearFormData, deleteManyData} from "@/utils/common/common";

export default {
  name: "Category",
  data() {
    return {
      //================= 初始化参数 =====================
      locale,
      loading: false,
      uploadLoading: false,
      hideSearch: false,
      saveOrUpdateDrawerShow: false,
      resetPasswordModelShow: false,
      drawerIsSave: null,
      timeValue: [],
      deleteList: [],
      typeList: [],
      total: 0,
      currentImages: [],
      basicTypeList: [],
      deviceTypeList: [],
      registeredSourceList: [],
      rules: {
        typeName: [{required: true, message: "请输入分类名！", trigger: 'blur'}],
        status: [{required: true, message: "请选择状态！", trigger: 'blur'}],
      },
      //===================  form表单  ==================
      condition: {
        pageNow: 1,
        pageSize: 8,
        typeName: undefined
        // gender: undefined,
        // roleId: undefined,
        // isDisabled: undefined,
        // loginType: undefined,
        // beginTime: undefined,
        // endTime: undefined
      },
      formData: {
        id: undefined,
        typeName: undefined,
        description: undefined,
        status: 1,
      },
    }
  },
  components: {
    PageTitle,
  },
  computed: {
    ...mapState(['userInfo']),
    rowSelectList() {
      return {
        onChange: (keys, rows) => {
          this.deleteList = keys;
        },
      };
    },
  },
  created() {
    this.initData();
    this.initSelect();
  },
  methods: {
    //=======================  初始化  ===========================
    async initData() {
      this.loading = true;
      const res = await listType(this.condition);
      if (res.success) {
        this.typeList = res.data.dataList;
        this.total = res.data.totalCount;
      }
      this.loading = false;
    },
    async initSelect() {
      // 获取类别下拉框信息
      const typeRes = await listTypeBasic();
      if (typeRes.success) {
        this.basicTypeList = typeRes.data;
      }

    },
    //=======================  交互方法  ===========================
    dateChange(date, dateString) {
      if (dateString[0] !== '' && dateString[1] !== '') {
        dateString[0] = dateString[0] + " 00:00:01";
        dateString[1] = dateString[1] + " 23:59:59";
      }
      this.condition.beginTime = dateString[0];
      this.condition.endTime = dateString[1];
    },
    resetCondition() {
      this.condition.typeName = undefined;
      this.condition.isDisabled = undefined;
      this.condition.beginTime = undefined;
      this.condition.endTime = undefined;
      this.timeValue = [];
      this.initData();
    },
    resetFormData() {
      //============== 分类表单 ===============
      this.saveOrUpdateDrawerShow = false;
      this.drawerIsSave = null;
      this.formData.id = undefined;
      this.formData.typeName = undefined;
      this.formData.isDisabled = 0;
      this.initData();
    },
    //=======================  新增/更新  ===========================
    toSaveOrUpdate(data) {
      this.drawerIsSave = data === null;
      this.formData = this.drawerIsSave ? this.formData : data;
      this.saveOrUpdateDrawerShow = true;
    },
    async doSaveOrUpdate() {
      this.$refs.formData.validate(async valid => {
        if (valid) {
          this.drawerIsSave ? checkResult(await saveType(this.formData)) : checkResult(await updateType(this.formData));
          this.resetFormData();
        }
      });
    },
    cancelSaveOrUpdate() {
      clearFormData(this.resetFormData);
    },
    //=======================  删除  ===========================
    deleteOne(userId) {
      deleteManyData("分类", batchDeleteType, [userId], this.initData);
    },
    deleteMany() {
      deleteManyData("分类", batchDeleteType, this.deleteList, this.initData);
    },
    //=======================  分页  ===========================
    sizeChange(current, size) {
      this.condition.pageSize = size;
      this.initData();
    },
    currentChange(current, size) {
      this.condition.pageNow = current;
      this.initData();
    },
    //=======================  上传  ===========================
    async uploadAvatar(info) {
      this.uploadLoading = true;
      const res = await uploadPicture(info.file, "/user", `/avatar/${this.userInfo.id}/`);
      if (res.success) {
        this.formData.typeUrl = res.data;
        this.uploadLoading = false;
        this.$notification.success({message: res.message});
      }
    },
    async uploadType(info) {
      this.uploadLoading = true;
      const res = await uploadPicture(info.file, "/user", `/type/${this.userInfo.id}/`);
      if (res.success) {
        this.formData.typeUrl = res.data;
        this.uploadLoading = false;
        this.$notification.success({message: res.message});
      }
    },
  }
}
</script>

<style lang="scss" scoped>

.tag-item {
  user-select: none;
  cursor: pointer;
}

.icon-item {
  margin-right: 10px;
}

</style>
