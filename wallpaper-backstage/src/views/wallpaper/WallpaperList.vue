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
          <!-- 壁纸名 -->
          <div class="search-item">
            <span class="label-title">壁纸名</span>
            <a-input class="label-component"
                     v-model="condition.imageName"
                     allowClear
                     placeholder="请输入壁纸名..."/>
          </div>
          <!-- 用户角色 -->
          <div class="search-item">
            <span class="label-title">类型</span>
            <a-select
                allowClear
                placeholder="请选类型..."
                class="label-component"
                v-model="condition.typeId">
              <a-select-option
                  v-for="item in basicTypeList"
                  :key="item.id"
                  :value="item.id">
                {{ item.typeName }}
              </a-select-option>
            </a-select>
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
          <a-button class="operate-left-item"
                    :disabled="deleteList.length === 0"
                    size="small"
                    @click="recoveryMany"
                    type="danger"
                    icon="reload">
            批量恢复
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
            :scroll="{x: 1650}"
            :rowSelection="rowSelectList"
            size="small"
            bordered
            :data-source="wallpaperList"
            :rowKey="(wallpaper) => wallpaper.id"
            :pagination="false">
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="壁纸名"
              data-index="imageName">
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
              data-index="typeName">
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
              data-index="imageUrl">
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
              data-index="description">
            <template slot-scope="description">
              <a-input type="textarea" readOnly :value="description"/>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="分辨率(宽*高)"
              data-index="imageSize">
            <template slot-scope="imageSize">
              <a-tooltip>
                <template slot="title">
                  {{ imageSize }}
                </template>
                {{ imageSize }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="图片大小(KB)"
              data-index="fileSize">
            <template slot-scope="fileSize">
              <a-tooltip>
                <template slot="title">
                  {{ fileSize }}
                </template>
                {{ fileSize }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="10%"
              ellipsis="true"
              title="下载次数"
              data-index="downloads">
            <template slot-scope="downloads">
              <a-tooltip>
                <template slot="title">
                  {{ downloads }}
                </template>
                {{ downloads }}
              </a-tooltip>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="7%"
              title="壁纸状态"
              data-index="isDeleted">
            <template slot-scope="isDeleted">
              <a-tag v-if="isDeleted === 1" size="small" color="#f50">删除</a-tag>
              <a-tag v-else-if="isDeleted === 0" size="small" color="#2db7f5">正常</a-tag>
            </template>
          </a-table-column>
          <a-table-column
              align="center"
              width="15%"
              title="审核状态"
              data-index="status"
          >
            <template slot-scope="status">
              <a-tag
                  v-for="item in dict.auditStatus"
                  :key="item.value"
                  v-if="status === item.value"
                  :color="item.color"
                  size="small"
              >
                {{ item.label }}
              </a-tag>
            </template>
          </a-table-column>
          <!--          <a-table-column
                        align="center"
                        width="15%"
                        title="上传时间"
                        data-index="createTime">
                      <template slot-scope="createTime">
                        <a-icon type="bell"/>
                        {{ createTime }}
                      </template>
                    </a-table-column>-->
          <a-table-column
              align="center"
              width="15%"
              title="上传时间"
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
                  v-if="scope.isDeleted === 0&&(scope.status === 0||scope.status === 3||scope.status === 4 ||scope.status === 5)"
                  style="margin-right: 10px"
                  size="small"
                  icon="edit"
                  @click="toSaveOrUpdate(scope)">
                编辑
              </a-button>
              <!-- 新添加的提交审核按钮-->
              <a-button
                  v-if="scope.status === 0 && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  type="primary"
                  @click="submitForReview(scope)">
                提交审核
              </a-button>
              <a-button
                  v-if="(scope.status === 3||scope.status === 4 ||scope.status === 5) && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  type="primary"
                  @click="submitForReview(scope)">
                重新提交
              </a-button>
              <!-- 新添加的撤回按钮 -->
              <a-button
                  v-if="scope.status === 1 && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  type="default"
                  @click="withdraw(scope.approvalId)">
                撤回
              </a-button>
              <a-button
                  v-if="scope.status === 2 && scope.isDeleted===0"
                  style="margin-right: 10px"
                  size="small"
                  type="default"
                  @click="reject(scope.approvalId)">
                驳回
              </a-button>
              <a-button
                  v-if="scope.isDeleted === 0&&(scope.status === 0||scope.status === 3||scope.status === 4 ||scope.status === 5)"
                  size="small"
                  type="danger"
                  icon="delete"
                  @click="deleteOne(scope.id)">
                删除
              </a-button>
              <a-button
                  v-if="scope.isDeleted === 1"
                  size="small"
                  type="danger"
                  icon="reload"
                  @click="recovery(scope.id)">
                恢复
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
          :show-total="(total) => `共有 ${total} 个壁纸`"
          @change="currentChange"
          @showSizeChange="sizeChange">
        <template slot="buildOptionText" slot-scope="props">
          <span v-if="props.value !== '50'">{{ props.value }}条/页</span>
        </template>
      </a-pagination>
    </div>

    <!-- 抽屉弹窗 -->
    <a-drawer
        :title="drawerIsSave ? '新增壁纸':'修改壁纸信息'"
        :width="680"
        :visible="saveOrUpdateDrawerShow"
        @close="cancelSaveOrUpdate">
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
              <a-input v-model="formData.imageName" placeholder="壁纸名..."/>
            </a-form-model-item>
          </a-col>
        </a-row>

        <!-- 分类/状态 -->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="分类：" prop="typeId">
              <a-select
                  allowClear
                  placeholder="请选分类..."
                  class="label-component"
                  v-model="formData.typeId">
                <a-select-option
                    v-for="item in basicTypeList"
                    :key="item.id"
                    :value="item.id">
                  {{ item.typeName }}
                </a-select-option>
              </a-select>
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
import {
  listWallpaper,
  recoveryWallpaper,
  saveWallpaper,
  updateWallpaper,
  batchDeleteWallpaper
} from "@/api/wallpaperrApi";
import {submit, reject, withdraw, batchReject, batchRefuse} from "@/api/approvalApi";
import {listTypeBasic} from "@/api/typeApi";
import {uploadPicture} from "@/api/uploadApi";
import {mapState} from "vuex";
import {checkResult} from "@/utils/result/resultUtil";
import {
  clearFormData,
  deleteManyData,
  recoveryData,
  refuseManyData,
  rejectManyData,
  withdrawManyData
} from "@/utils/common/common";
import {dict} from "@/utils/dict/dict";
import {Modal, Input, notification} from 'ant-design-vue';
import {h} from 'vue';

export default {
  name: "Wallpaper",
  data() {
    return {
      myObject: {
        name: '示例对象',
        code: 'ABC123',
        // 其他属性...
      },
      reviewRemarks: null, // 用于保存审核备注信息
      dict: dict,
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
      wallpaperList: [],
      total: 0,
      currentImages: [],
      basicTypeList: [],
      deviceTypeList: [],
      registeredSourceList: [],
      rules: {
        imageName: [{required: true, message: "请输入壁纸名！", trigger: 'blur'}],
        imageUrl: [{required: true, message: "请上传壁纸！", trigger: 'blur'}],
        typeId: [{required: true, message: "请选择分类！", trigger: 'blur'}],
      },
      //===================  form表单  ==================
      condition: {
        pageNow: 1,
        pageSize: 8,
        imageName: undefined
        // gender: undefined,
        // roleId: undefined,
        // isDisabled: undefined,
        // loginType: undefined,
        // beginTime: undefined,
        // endTime: undefined
      },
      formData: {
        id: undefined,
        imageUrl: undefined,
        imageName: undefined,
        description: undefined,
        typeId: undefined,
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
      const res = await listWallpaper(this.condition);
      if (res.success) {
        this.wallpaperList = res.data.dataList;
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
      this.condition.imageName = undefined;
      this.condition.typeId = undefined;
      this.condition.isDisabled = undefined;
      this.condition.beginTime = undefined;
      this.condition.endTime = undefined;
      this.timeValue = [];
      this.initData();
    },
    resetFormData() {
      //============== 壁纸表单 ===============
      this.saveOrUpdateDrawerShow = false;
      this.drawerIsSave = null;
      this.formData.id = undefined;
      this.formData.imageUrl = undefined;
      this.formData.imageName = undefined;
      this.formData.typeId = undefined;
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
          this.drawerIsSave ? checkResult(await saveWallpaper(this.formData)) : checkResult(await updateWallpaper(this.formData));
          this.resetFormData();
        }
      });
    },
    cancelSaveOrUpdate() {
      clearFormData(this.resetFormData);
    },
    //=======================  恢复  ===========================
    recovery(id) {
      recoveryData("壁纸", recoveryWallpaper, [id], this.initData);
    },
    recoveryMany() {
      recoveryData("壁纸", recoveryWallpaper, this.deleteList, this.initData);
    },
    //=======================  删除  ===========================
    deleteOne(id) {
      deleteManyData("壁纸", batchDeleteWallpaper, [id], this.initData);
    },
    deleteMany() {
      deleteManyData("壁纸", batchDeleteWallpaper, this.deleteList, this.initData);
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
    renderContent(h, wallpaper) {
      return h('div', {
        style: {
          textAlign: 'center',
        },
      }, [
        h('div', {
          style: {
            marginBottom: '10px',
          },
        }),
        h('div', {
          style: {
            marginBottom: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          },
        }, [
          h('label', {
            style: {
              fontSize: '14px',
              marginBottom: '5px',
              marginRight: '10px',
              display: 'inline-block',
              minWidth: '80px',
            },
          }, '审核备注：'),
          h('div', {
            style: {
              display: 'flex',
              alignItems: 'center',
            },
          }, [
            h('input', {
              style: {
                flex: '1',
                padding: '5px 10px',
                fontSize: '14px',
                border: '1px solid #ccc',
                borderRadius: '4px',
                outline: 'none',
              },
              domProps: {
                value: this.reviewRemarks,
                placeholder: '请输入审核备注',
              },
              on: {
                input: event => {
                  this.reviewRemarks = event.target.value;
                },
              },
            }),
          ]),
        ]),
      ]);
    },


    //=======================  审核相关  ===========================
    // 提交审核
    submitForReview(wallpaper) {
      Modal.confirm({
        title: '提交审核',
        content: h => this.renderContent(h, wallpaper),
        onOk: () => {
          // 在这里调用提交审核的 API，根据实际情况替换为实际的 API 调用
          submit(wallpaper.id, 'wallpaper', this.reviewRemarks)
              .then(() => {
                this.$notification.success({message: '提交审核成功'});
                this.initData(); // 刷新数据
              })
              .catch(error => {
                this.$notification.error({message: '提交审核失败', description: error.message});
              });
        },
        onCancel: () => {
          // 取消操作
        },
      });
    },
    // 批量驳回
    reject(id) {
      rejectManyData('壁纸', batchReject, [id], this.initData)
    },
    batchReject() {
      rejectManyData('壁纸', batchReject, this.selectList, this.initData)
    },
    // 拒绝
    withdraw(id) {
      withdrawManyData("壁纸", withdraw, [id], this.initData)
    },
    //=======================  上传  ===========================
    async uploadAvatar(info) {
      this.uploadLoading = true;
      const res = await uploadPicture(info.file, "/user", `/avatar/${this.userInfo.id}/`);
      if (res.success) {
        this.formData.imageUrl = res.data;
        this.uploadLoading = false;
        this.$notification.success({message: res.message});
      }
    },
    async uploadWallpaper(info) {
      this.uploadLoading = true;
      const res = await uploadPicture(info.file, "/user", `/wallpaper/${this.userInfo.id}/`);
      if (res.success) {
        this.formData.imageUrl = res.data;
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
