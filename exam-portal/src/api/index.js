const api = {
  Login: '/auth/login',
  Logout: '/auth/logout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/account/sms',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/user/info',

  // 下面是自己的用户认证的接口
  UserRegister: '/user/register',
  UserLogin: '/user/login',
  UserList: '/user/list',
  UserUpdate: '/user/update',

  // 考试的接口
  ExamQuestionList: '/question/list',
  ExamQuestionAll: '/question/all',
  ExamQuestionUpdate: '/question/update',
  ExamQuestionSelection: '/question/selection',
  ExamQuestionCreate: '/question/create',
  ExamList: '/exam/list',
  ExamAll: '/exam/all',
  // 获取问题列表，按照单选、多选和判断进行分类
  ExamQuestionTypeList: '/question/type/list',
  ExamCreate: '/exam/create',
  ExamRandomCreate: '/exam/random',
  ExamUpdate: '/exam/update',
  ExamCardList: '/exam/card/list',
  // 获取考试详情
  ExamDetail: '/exam/detail/',
  // 获取考试详情
  QuestionDetail: '/question/detail/',
  // 交卷
  FinishExam: '/exam/finish/',
  ExamRecordList: '/record/list',
  ExamRecordListByExam: '/record/listByExam/',
  recordDetail: '/record/detail/',
  ExamPercentage: '/record/percentage/',
  ExamLevel: '/record/level/',
  ExamRecord: '/record/'
}
export default api
