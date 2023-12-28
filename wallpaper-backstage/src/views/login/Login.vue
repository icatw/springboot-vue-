<template>
  <div class="main-container"
       :style="`background: url(${Backstage}) center center / cover no-repeat;`">
    <!-- 登录部分 -->
    <div class="login-container">
      <!-- 登录界面 -->
      <div class="login-box">
        <!-- 标题部分 -->
        <div class="title-span">
          <img class="logo" :src="backstageInfo.logo">
          <span class="title">{{ backstageInfo === null ? "后台管理系统" : backstageInfo.websiteTitle }}</span>
        </div>
        <!-- 登录表单 -->
        <a-form-model class="login" ref="ruleForm" @submit="login('ruleForm')" @submit.native.prevent :model="loginForm"
                      :rules="rules">
          <a-form-model-item prop="username">
            <a-input v-model="loginForm.username" placeholder="请输入用户名..."/>
          </a-form-model-item>
          <a-form-model-item prop="password">
            <a-input-password v-model="loginForm.password" placeholder="请输入密码..."/>
          </a-form-model-item>
          <!-- 滑块验证码 -->
          <a-form-model-item prop="captcha">
            <SliderVerificationCode height="35px"
                                    content="请拖动滑块进行验证"
                                    inactiveValue="未验证"
                                    activeValue="已验证"
                                    v-model="isPass"/>
          </a-form-model-item>
          <div class="remember-me">
            <a-checkbox dio>记住我</a-checkbox>
            <a href="#">忘记密码</a>
          </div>
          <a-form-model-item style="display:flex;justify-content: space-around">
            <a-button
                class="login-button"
                type="primary"
                html-type="submit"
                @submit.native.prevent
                :disabled="loginForm.username === '' || loginForm.password === ''"
            >
              <a-icon type="login"/>
              登陆
            </a-button>
            <a-button
                class="login-button"
                @click="isShowQrPage = true"
                type="primary">
              <a-icon type="reddit"/>
              获取账号
            </a-button>
          </a-form-model-item>
        </a-form-model>
        <!--第三方登陆-->
        <a-divider style="margin: -15px 0 10px 0">第三方登陆</a-divider>
        <div class="three-box">
<!--          <a-button type="primary" shape="circle" icon="Gitee"></a-button>-->
          <a-button type="primary" @click="thirdPartyLogin('gitee')" shape="circle"><i class="iconfont icon-gitee"></i></a-button>
          <a-button type="primary" @click="thirdPartyLogin('github')" shape="circle"><i class="iconfont icon-social-github"></i></a-button>
<!--          <a-button type="primary" shape="circle" icon="qq"></a-button>-->
<!--          <a-button type="primary" shape="circle" icon="alipay" style="margin-left: 10px"></a-button>-->
        </div>
      </div>
      <!-- 页脚部分 -->
      <div class="footer-span">
        <a-divider>{{ backstageInfo === null ? "Copyright © 2023 by ~ 段楠" : backstageInfo.copyright }}</a-divider>
      </div>
    </div>
    <!--  其他部分 -->
    <div class="other-container">
      <!-- 网站标题 -->
      <div class="website-detail-title">
        <span>{{ backstageInfo === null ? "后台管理系统" : backstageInfo.websiteName }}</span>
      </div>
      <!-- 网站标语 -->
      <div class="website-desc">
        <span>———&emsp;{{ backstageInfo === null ? "生如芥子，心藏须弥。" : backstageInfo.websitePoster }}&emsp;———</span>
      </div>
    </div>

    <!-- 公众号弹窗 -->
    <a-modal
        title="获取体验账号提示信息"
        :visible="isShowQrPage"
        @ok="isShowQrPage =  false"
        :footer="null">
      <div style=" width: 100%;text-align: center;font-size: 18px;">
        <span style="color: #48a2ff;">
        添加微信
      </span>
        回复
        <span style="color: #48a2ff;">后台管理系统体验</span>
        获取体验账号
      </div>
      <div style="width: 100%;margin: 20px 0;display: flex;justify-content: center">
        <img :src="QrCode" alt="..." style="width: 50%;">
      </div>


      <div style="width: 100%;margin: 20px 0;display: flex;justify-content: center">
        <a-button type="primary" @click="isShowQrPage = false" style="width: 60%">
          确定
        </a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>


import Backstage from "@/assets/images/backstage.jpg";
import Logo from "@/assets/images/logo/logo.png";
import {socialLogin, userLogin} from "@/api/systemApi";
import {mapMutations} from "vuex";
import {getWebsiteConfig} from "@/api/websiteConfigApi";
import QrCode from "@/assets/images/qr/QRCode.png";
import '@/assets/iconfont/iconfont.css'
import {notification} from "ant-design-vue";

export default {
  name: "Login",
  data() {
    return {
      //==============   初始化   ===============
      rules: {
        username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空', trigger: 'blur'}],
      },
      backstageInfo: {},
      isPass: false,
      //==============   参数对象   ===============
      loginForm: {
        username: "user",
        password: "123456",
      },
      Backstage: Backstage,
      Logo: Logo,
      QrCode: QrCode,
      isShowQrPage: false,
    }
  },
  created() {
    this.initData();
  },
  methods: {
    ...mapMutations(['UPDATE_USERINFO']),
    //================== 初始化方法 ===================
    async initData() {
      const res = await getWebsiteConfig(2);
      if (res.success) {
        this.backstageInfo = res.data;
        this.Backstage = this.backstageInfo.background;
      }
    },
    //================== 登陆方法 ==================
    // 三方登录
    async thirdPartyLogin (platform) {
      console.log(`使用${platform}登录`)
      notification.warn({message: '后台三方登录开发中...'});
      // let res = await socialLogin(platform)
      //获取到后端传递过来的授权路径
      //跳转到gitee授权页
      // window.location.href = res.data.url
    },
    login(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.isPass) {
            userLogin(this.loginForm).then(async res => {
              if (res.success) {
                window.sessionStorage.setItem('Authorization', res.data);
                this.$notification.success({message: res.message});
                this.$router.replace('/welcome');
              }
            });
          } else {
            this.$message.error("未通过滑块验证");
          }
        }
      });
    },


  }
}
</script>

<style lang="scss" scoped>
.main-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;

  .login-container {
    position: relative;
    width: 26%;
    height: 100%;
    opacity: 0.95;
    background: white;
    border-right: 1px solid gray;
    border-radius: 10px;
    box-shadow: 2px 10px 20px gray;


    .login-box {
      position: absolute;
      width: 100%;
      height: auto;
      padding: 10px 0 20px 0;
      margin: 0 auto;
      top: 15%;

      .title-span {
        display: flex;
        justify-content: center;
        margin-bottom: 40px;

        .logo {
          user-select: none;
          width: 38px;
          height: 38px;
          margin-right: 10px;
          border-radius: 50%;
        }

        .title {
          user-select: none;
          font-size: 24px;
          font-weight: bolder;

        }
      }

      .login {
        padding: 0 30px;

        .remember-me {
          display: flex;
          justify-content: space-between;
        }

        .login-button {
          margin: 10px 10px 0 0;
        }
      }

      .three-box {
        display: flex;
        justify-content: center;
      }

    }


    .footer-span {
      user-select: none;
      position: absolute;
      bottom: 1%;
      left: 0;
      right: 0;
    }
  }

  .other-container {
    width: 74%;
    position: relative;

    .website-detail-title {
      position: absolute;
      width: 100%;
      text-align: center;
      top: 8%;

      span {
        color: white;
        user-select: none;
        font-weight: bolder;
        font-size: 40px;
      }
    }

    .website-desc {
      position: absolute;
      width: 100%;
      text-align: center;
      bottom: 5%;


      span {
        user-select: none;
        font-size: 20px;
        color: white;
      }
    }
  }

}
</style>
