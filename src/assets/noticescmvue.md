<template>
  <dl id="noticeInfoWrap">
    <dd class="content"></dd>
    <!-- s : 여기에 내용입력 -->
    <table id="noticeInfo">
      <caption>
        caption
      </caption>
      <colgroup>
        <col width="120px" />
        <col width="200px" />
        <col width="120px" />
        <col width="200px" />
      </colgroup>

      <tbody>
        <tr>
          <td colspan="4" class="text-center">
            <div class="my-4">
              <strong style="font-size: 30px">{{ title }}</strong>
            </div>
          </td>
        </tr>
        <tr v-if="paction == 'U'">
          <th scope="row">번호 <span class="font_red">*</span></th>
          <td>
            <input
              type="text"
              class="form-control"
              name="noticeNo"
              id="noticeNo"
              readonly
              v-model="noticeNo"
            />
          </td>
          <th scope="row">조회수</th>
          <td>
            <input
              type="text"
              class="form-control"
              name="noticeViewCnt"
              id="noticeViewCnt"
              readonly
              v-model="noticeViewCnt"
            />
          </td>
        </tr>
        <tr>
          <th scope="row">제목<span class="font_red">*</span></th>
          <td colspan="3">
            <input
              type="text"
              class="form-control"
              name="noticeTitle"
              id="noticeTitle"
              @keyup="handleTitle()"
              ref="title"
              v-model="noticeTitle"
            />
          </td>
        </tr>
        <tr>
          <th scope="row">내용<span class="font_red">*</span></th>
          <td colspan="3">
            <textarea
              class="form-control"
              name="noticeContent"
              id="noticeContent"
              @keyup="handleContent()"
              ref="content"
              rows="8"
              v-model="noticeContent"
            ></textarea>
          </td>
        </tr>
        <tr v-if="paction == 'I'">
          <th scope="row">첨부파일</th>
          <td colspan="3">
            <input type="file" class="inputTxt p100" @change="handleAttach" ref="attachImage" accept="image/*" />
          </td>
        </tr>
        <tr v-if="paction == 'U' && fileRelativePath">
          <th scope="row">첨부파일</th>
          <td colspan="3">
            <input type="text" class="" readonly v-model="fileName"/>&nbsp;
            <a class="" id="download" :href="fileRelativePath" download>
                <button class="">다운로드</button>
            </a>
          </td>
        </tr>
        <tr>
          <th scope="row">열람권한</th>
          <td colspan="3">
            <select
              id="noticeAuth"
              class="form-control"
              style="width: 150px"
              v-model="noticeAuth"
            >
              <option value="0">전체</option>
              <option value="1">직원</option>
            </select>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- e : 여기에 내용입력 -->

    <div class="btn_areaC mt30">
      <a @click="save()" class="btn btn-primary" id="btnSaveGrpCod" name="btn">
        <span>저장</span>
      </a>
      <a @click="deleteNotice()" class="btn btn-danger mx-2" v-show="delshow">
        <span>삭제</span>
      </a>
      <a @click="cancel()" :class="{'btn btn-primary mx-2': delshow == false, 'btn btn-primary': delshow == true}">
        <span>취소</span>
      </a>
    </div>
  </dl>
</template>

<script>
import { closeModal } from "jenesius-vue-modal";
export default {
  // vue에서는 받아온 변수를 methods에서 직접 핸들링이 불가능하기 때문에
  // 임시 변수를 만들어서 받아온 변수를 넣어 줘야 함
  props: { title: String, noticeId: Number, action: String },
  data: function () {
    return {
      noticeNo: this.noticeId,
      paction: this.action,
      loginId: "",
      noticeTitle: "",
      noticeContent: "",
      noticeRegdate: "",
      noticeAuth: "0",
      noticeViewCnt: 0,
      saveUrl: "",
      loginName: "",
      delshow: true,
      image: '',
      fileName: '',
      fileRelativePath: '',
    };
  },
  computed: {},
  // html 로딩, 가상 dom 실행, 이 두 개 연결 시 작동
  mounted: function () {
    let vm = this;
    // 신규 등록 시
    if (this.noticeNo == null || this.noticeNo == "") {
      vm.noticeNo = "";
      vm.noticeTitle = "";
      vm.noticeContent = "";
      vm.noticeRegdate = "";
      vm.noticeAuth = "0";
      vm.loginName = "";

      vm.delshow = false;
    } else {
      //  수정 시 (noticeNo 에 해당하는 상세정보 가져오기)
      let params = new URLSearchParams();
      //alert(this.noticeNo);
      params.append("notice_id", this.noticeNo);

      this.axios
        .post("/system/detailNotice.do", params)
        .then(function (response) {
          console.log(response);

          vm.noticeNo = response.data.notice_id;
          vm.noticeTitle = response.data.title;
          vm.noticeContent = response.data.content;
          vm.noticeRegdate = response.data.date;
          vm.noticeAuth = response.data.auth;
          vm.noticeViewCnt = response.data.view_cnt;
          vm.fileName = response.data.file_ofname;
          vm.fileRelativePath = response.data.file_relative_path;
          vm.delshow = true;
        })
        .catch(function (error) {
          alert("에러! API 요청에 오류가 있습니다. " + error);
        });
    }
  },
  methods: {
    save: function () {
      if(!this.validateIsNull()) {
        return;
      }

      if (confirm("저장하시겠습니까?")) {
        let vm = this;
        /*
        let params = new URLSearchParams();
        
        params.append("title", this.noticeTitle);
        params.append("content", this.noticeContent);
        params.append("auth", this.noticeAuth);
        //params.append("file_no", '');

        if(this.paction == "I") {
          this.saveUrl = "/system/writeNotice.do"
        } else if(this.paction == "U") {
          params.append("notice_id", this.noticeNo);
          params.append("file_nm", "");
          params.append("noFile", "noFile");
          this.saveUrl = "/system/modifyNotice.do"
        }
        */
        const formData = new FormData();
        formData.append("title", this.noticeTitle);
        formData.append("content", this.noticeContent);
        formData.append("auth", this.noticeAuth);
        if(this.image.length > 0) {
          formData.append("file", this.image[0]);
          formData.append("isFile", "isFile");
        }

        //formData.append("noFile", "noFile");
        console.log('image:::' +this.image[0]);
        console.log('fileName:::' +this.image[0].name);

        if(this.paction == "I") {
          this.saveUrl = "/system/writeNotice.do"
        } else if(this.paction == "U") {
          formData.append("notice_id", this.noticeNo);
          formData.append("file_nm", this.image[0].name);
          this.saveUrl = "/system/modifyNotice.do"
        }

        this.axios({
            method: "post",
            url: this.saveUrl,
            headers:{
              'Content-Type': 'multipart/form-data',
            },
            data: formData
          })
          .then(function (response) {
            console.log(response);
            let status = response.status;
            //let msg = response.data.resultMsg;
            if (status == 200) {
              alert("저장이 완료되었습니다.");
              closeModal(vm);
            } else {
              alert("API 요청에 오류가 있습니다.");
            }
          })
          .catch(function (error) {
            alert("에러! API 요청에 오류가 있습니다. " + error);
          });
/*
        this.axios
          .post(this.saveUrl, params)
          .then(function (response) {
            console.log(response);
            let status = response.status;
            //let msg = response.data.resultMsg;
            if (status == 200) {
              alert("저장이 완료되었습니다.");
              closeModal(vm);
            } else {
              alert("API 요청에 오류가 있습니다.");
            }
          })
          .catch(function (error) {
            alert("에러! API 요청에 오류가 있습니다. " + error);
          });
          */
      }
    },
    deleteNotice: function () {
      if (confirm("정말 삭제하시겠습니까?")) {
        let vm = this;
        let params = new URLSearchParams();
        params.append("notice_id", this.noticeNo);
        params.append("file_no", 0);
        params.append("file_nm", "");

        this.axios
          .post("/system/deleteNotice.do", params)
          .then(function (response) {
            console.log(response);
            //alert("삭제 되었습니다.");
            closeModal(vm);
          })
          .catch(function (error) {
            alert("에러! API 요청에 오류가 있습니다. " + error);
          });
      }
    },
    cancel: function() {
      let vm = this;
      closeModal(vm);
    },
    validateIsNull: function () {
      let chk = this.checkNotEmpty([
        ["noticeTitle", "제목을  입력해주세요."],
        ["noticeContent", "내용을  입력해주세요."],
      ]);
      return chk;
    },
    checkNotEmpty: function (arr) {
      for (var i = 0, len = arr.length; i < len; i++) {
        var elem = document.getElementById(arr[i][0]);
        console.log("elem is...");
        console.log(elem);
        if (elem.length <= 0) {
          continue;
        }
        var elemValue = elem.value;
        var alertMsg = arr[i][1];

        console.log(elemValue);
        if (elemValue == "") {
          alert(alertMsg);
          elem.focus();
          return false;
        }
      }
      return true;
    },
    handleTitle: function() {
      if(this.noticeTitle.length>=100) {
        alert('제목은 100자 이하로 작성해 주세요');
        this.$refs.title.value = this.noticeTitle.substring(0,100);
      }
      return false;
    },
    handleContent: function() {
      if(this.noticeContent.length>=1000) {
        alert('내용은 1000자 이하로 작성해 주세요');
        this.$refs.content.value = this.noticeContent.substring(0,1000);
      }
      return false;
    },
    handleAttach: function () {
      this.image = this.$refs.attachImage.files;
      console.log(this.image);
    }
  }
};
</script>

<style>
#noticeInfo {
  border-collapse: separate;
  border-spacing: 20px;
}
#noticeInfoWrap {
  background-color: #fff;
  padding: 3rem;
  border-radius: 10px;
  border: 2px solid rgb(59, 59, 59);
}
</style>
