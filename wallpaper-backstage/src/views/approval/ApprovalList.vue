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
            <span class="label-title">业务模块</span>
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
          <!--          <a-button class="operate-left-item"-->
          <!--                    size="small"-->
          <!--                    type="primary"-->
          <!--                    icon="plus"-->
          <!--                    @click="toSaveOrUpdate(null)">-->
          <!--            新增-->
          <!--          </a-button>-->
          <a-button class="operate-left-item"
                    :disabled="selectList.length === 0"
                    size="small"
                    @click="batchApproval"
                    type="primary"
                    icon="check">
            批量同意
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
            :data-source="approvalList"
            :rowKey="(approval) => approval.id"
            :pagination="false">
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="壁纸名"
              data-index="businessEntity.imageName">
            <template slot-scope="imageName">
              <a-tooltip>
                <template slot="title">
                  {{ imageName }}
                </template>
                {{ imageName }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="所属分类"
              data-index="businessEntity.typeName">
            <template slot-scope="typeName">
              <a-tag color="blue" class="tag-item">
                {{ typeName }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="8%"
              title="图片"
              data-index="businessEntity.imageUrl">
            <template slot-scope="imageUrl">
              <viewer :images="[imageUrl]">
                <a-avatar style="cursor: pointer" shape="square" size="large" :src="imageUrl" alt="无"/>
              </viewer>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="描述"
              data-index="businessEntity.description">
            <template slot-scope="description">
              <a-tooltip>
                <template slot="title">
                  {{ description }}
                </template>
                {{ description }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              ellipsis="true"
              title="提交人账号"
              data-index="submitterName">
            <template slot-scope="submitterName">
              <a-tag color="#55acee">
                <a-icon type="user"/>
                {{ submitterName }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              title="提交时间"
              data-index="gmtUpdate">
            <template slot-scope="gmtUpdate">
              <a-icon type="bell"/>
              {{ gmtUpdate }}
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="备注"
              data-index="remarks">
            <template slot-scope="remarks">
              <a-tooltip>
                <template slot="title">
                  {{ remarks }}
                </template>
                {{ remarks }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              ellipsis="true"
              title="审批状态"
              data-index="approvalStatus">
            <template slot-scope="approvalStatus">
              <a-tag
                  v-for="item in dict.auditStatus"
                  :key="item.value"
                  v-if="approvalStatus === item.value"
                  :color="item.color"
                  size="small"
              >
                {{ item.label }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              title="审核通过时间"
              data-index="approvalTime">
            <template slot-scope="approvalTime">
              <a-icon type="bell"/>
              {{ approvalTime }}
            </template>
          </a-table-column>
          <a-table-column
              width="280px"
              align="center"
              fixed="right"
              title="操作">
            <template slot-scope="scope">
              <a-button
                  style="margin-right: 10px"
                  size="small"
                  icon="edit"
                  @click="details(scope)">
                查看详情
              </a-button>
              <a-button
                  v-if="scope.approvalStatus === 1 && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  type="primary"
                  icon="check"
                  @click="agree(scope.id)">
                同意
              </a-button>
              <a-button
                  v-if="scope.approvalStatus === 1 && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  icon="close"
                  type="danger"
                  @click="refuse(scope.id)">
                拒绝
              </a-button>
              <a-button
                  v-if="scope.approvalStatus === 2 && scope.isDeleted===0"
                  size="small"
                  type="danger"
                  icon="delete"
                  @click="reject(scope.id)">
                驳回
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
        title="壁纸详情"
        :width="680"
        :visible="detailsDrawerShow"
        @close="cancel">
      <a-form-model
          ref="formData"
          :model="formData"
          :rules="rules"
          layout="vertical">
        <!-- 壁纸头像 -->
        <a-row :gutter="16">
          <a-form-model-item label="图片：" prop="imageUrl">
            <a-upload name="file"
                      class="upload-item-avatar"
                      :show-upload-list="false"
                      :disabled="true"
                      accept="image/*"
                      :customRequest="uploadAvatar">
              <img class="img-item"
                   :src="formData.imageUrl"
                   v-if="formData.imageUrl !== undefined && formData.imageUrl !== ''"
                   alt="avatar"/>
              <div class="text-item" v-else>
                <a-icon :type="uploadLoading ? 'loading' : 'contacts'"/>
                <div class="ant-upload-text">图片</div>
              </div>
            </a-upload>
          </a-form-model-item>
        </a-row>

        <!-- 壁纸名/昵称 -->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="壁纸名：" prop="imageUrl">
              <a-input v-model="formData.imageName" readonly placeholder="壁纸名..."/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="分类：" prop="typeName">
              <a-tag color="blue" class="tag-item">
                {{ formData.typeName }}
              </a-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <!-- 壁纸名/昵称 -->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="分辨率(宽*高)：" prop="imageSize">
              <a-tag color="blue" class="tag-item">
                {{ formData.imageSize }}
              </a-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="图片大小(KB)：" prop="fileSize">
              <a-tag color="blue" class="tag-item">
                {{ formData.fileSize }}
              </a-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-form-model-item label="描述">
            <a-input readonly v-model="formData.description" type="textarea" :rows="8"
                     placeholder="请输入描述"/>
          </a-form-model-item>
        </a-row>
      </a-form-model>

      <div class="drawer-footer-btn">
        <a-button style="margin-right: 10px" @click="cancel">取消</a-button>
        <a-button type="primary" @click="cancel">确定</a-button>
      </div>
    </a-drawer>
  </a-card>
</template>

<script>
import PageTitle from "@/components/layout/page/PageTitle";
import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";
import {batchApproval, listApproval, batchReject, batchRefuse} from "@/api/approvalApi";
import {listTypeBasic} from "@/api/typeApi";
import {uploadPicture} from "@/api/uploadApi";
import {mapState} from "vuex";
import {checkResult} from "@/utils/result/resultUtil";
import {approvalManyData, clearFormData, deleteManyData, refuseManyData, rejectManyData} from "@/utils/common/common";
import {dict} from "@/utils/dict/dict";

export default {
  name: "ApprovalList",
  data() {
    return {
      //================= 初始化参数 =====================
      locale,
      loading: false,
      uploadLoading: false,
      hideSearch: false,
      detailsDrawerShow: false,
      resetPasswordModelShow: false,
      drawerIsSave: null,
      timeValue: [],
      selectList: [],
      approvalList: [],
      total: 0,
      currentImages: [],
      basicTypeList: [],
      deviceApprovalList: [],
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
        // loginApproval: undefined,
        // beginTime: undefined,
        // endTime: undefined
      },
      formData: {
        id: undefined,
        typeName: undefined,
        fileSize: null,
        imageSize: null,
        description: undefined,
        status: 1,
      },
    }
  },
  components: {
    PageTitle,
  },
  computed: {
    dict() {
      return dict
    },
    ...mapState(['userInfo']),
    rowSelectList() {
      return {
        onChange: (keys, rows) => {
          this.selectList = keys;
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
      const res = await listApproval(this.condition);
      if (res.success) {
        this.approvalList = res.data.dataList;
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
      this.detailsDrawerShow = false;
      this.drawerIsSave = null;
      this.formData.id = undefined;
      this.formData.typeName = undefined;
      this.formData.isDisabled = 0;
      this.initData();
    },
    // 详情/同意/驳回
    details(data) {
      this.formData = data.businessEntity;
      this.detailsDrawerShow = true;
    },
    // 同意
    agree(id) {
      approvalManyData("壁纸", batchApproval, [id], this.initData)
    },
    // 批量同意
    batchApproval() {
      approvalManyData("壁纸", batchApproval, this.selectList, this.initData)
    },
    // 拒绝
    refuse(id) {
      refuseManyData("壁纸", batchRefuse, [id], this.initData)
    },
    // 批量驳回
    reject(id) {
      rejectManyData('壁纸', batchReject, [id], this.initData)
    },
    batchReject() {
      rejectManyData('壁纸', batchReject, this.selectList, this.initData)
    },
    //=======================  新增/更新  ===========================
    // toSaveOrUpdate(data) {
    //   this.drawerIsSave = data === null;
    //   this.formData = this.drawerIsSave ? this.formData : data;
    //   this.detailsDrawerShow = true;
    // },
    // async doSaveOrUpdate() {
    //   this.$refs.formData.validate(async valid => {
    //     if (valid) {
    //       this.drawerIsSave ? checkResult(await saveApproval(this.formData)) : checkResult(await updateApproval(this.formData));
    //       this.resetFormData();
    //     }
    //   });
    // },
    cancel() {
      this.resetFormData();
    },
    //=======================  删除  ===========================
    // deleteOne(userId) {
    //   deleteManyData("分类", batchDeleteApproval, [userId], this.initData);
    // },
    // deleteMany() {
    //   deleteManyData("分类", batchDeleteApproval, this.selectList, this.initData);
    // },
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
    async uploadApproval(info) {
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
