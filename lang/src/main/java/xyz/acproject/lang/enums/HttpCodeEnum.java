package xyz.acproject.lang.enums;

/**
 * @author Jane
 * @ClassName HttpCodeEnum
 * @Description TODO
 * @date 2021/1/27 16:57
 * @Copyright:2021 v1.0
 */
public enum HttpCodeEnum {

    //Commonly used 通用 1000以下
    success(0, "success", "操作成功"),
    normal(200, "successful", "操作成功"),
    error(-200, "error", "操作失败"),
    repeatedsubmit(-203, "repeated submit!", "重复的提交"),
    notfound(404,"404 not found","请求页面或接口不存在"),
    bussy(-500, "system is busy!", "系统繁忙"),
    syserror(-400, "system error!", "系统错误"),
    tokenerror(-302, "No Token!", "无token"),
    unsupportMediaType(-415, "unsupported media type", "不支持媒体类型"),
    tokenfail(-403, "Token validation failed!", "token验证失败,过期或失效"),
    systemforbidden(403, "System forbidden to access!", "系统禁止访问!"),
    insufficientpermissions(402,"Insufficient permissions!","权限不足"),
    tokenexpired(-303, "Token Expired!", "token已过期"),
    paramserror(301, "Requested parameter error!", "请求参数错误"),
    urlerror(302, "Requested url error!", "请求url拼写错误"),
    requestmethoderror(-413,"Request method not supported","不支持的请求类型"),
    custom(-200,"unknown error","未知错误"),
    accountinfonocompletion(109, "User info not completed", "用户信息未补全"),
    filenoempty(316,"file must be no empty!","文件非空"),
    filesuffixnosupport(317,"file suffix no support!","文件后缀不支持"),
    expired_request(-10,"expired request","超时请求"),
    expired_remote_request(-11,"expired remote request","超时的远程请求"),
    error_remote_request(-12,"unknown error from remote interface","未知远程接口错误"),
    request_timeout(408,"request handle timeout","请求处理超时"),
    payload_too_large(413,"payload too large","请求体超出系统限制大小"),

    //数据支持
    datanotexist(-101,"data does not exist","数据不存在"),
    dataalreadyexist(-102,"data already exists","数据已经存在"),

    threadisrunning(516,"thread is running","程序运行中"),

    phonematcherror(-150,"phone check error,please check again","电话号码错误，清检查一遍"),
    usernamematcherror(-151,"username check error,please check again","用户名称填写错误，请检查一遍"),
    //RSA
    rsaverifyerror(-350,"verify error","验签失败"),

    //支付相关
    pay_error(-600,"pay error","支付错误"),
    pay_not(-601,"not pay","未支付"),
    balance_insufficient(-602,"insufficient balance","余额不足"),

    //登录相关
    accountnotexist(101, "User does not exist.", "用户不存在"),
    accountremoved(102, "User has been disabled.", "用户已停用"),
    passworderror(103, "Account or password error.", "账号或密码错误"),
    similarpassword(104,"similar password with old password!","新密码不能与与原密码一致"),
    passwordinconsistent(104,"password inconsistent","密码不一致"),
    authcodeexpired(105,"code was expired","验证码已过期"),
    authcodeerror(106,"code error","验证码错误"),
    ///wx
    wxsecuritycheckerror(-603,"wx security check error","微信检查异常"),

    //touch 10xx & - 10xx
    activity_nostart(1011,"activity has not started yet","活动还没开始"),
    activity_end(1012,"activity has ended","活动已经结束"),
    activity_ing(1013,"activity starting","活动进行中"),
    //ab 农行对接相关 104xx & -104xx
    ab_api_error(10401,"ab token get error!","农行授权接口错误"),
    ab_code_expired(-10401,"ab auth code expired","code过期"),
    //touch_lottery (touch抽奖) 105xx & -105xx
    lottery_count_Insufficient(10501,"Insufficient number of draws available","可用抽奖次数不足"),
    integral_error(10502,"integral error","积分错误"),
    lottery_busy(10503,"lottery is busy,please visit later","活动太火爆了，请过后再访问"),
    lottery_pirze_waiting_claimed(-10554,"the prize waiting to be claimed","奖品等待领取中"),
    lottery_ab_white(-10551,"Non-ab whitelist","非农行指定用户白名单"),
    lottery_pirze_has_been_claimed(-10552,"the prize has been claimed","该奖品已经被领取"),


    // xiangzhoubu 1060x & 1061x & -
    reserve_full(10601,"reserve full","预约满了"),
    reserved(10602,"already reserved","已经预约了"),
    restnow(10603,"today is rest","休息中"),

    // 芬达 1062x & 1063x & -
    area_not_select(10621,"area not select","地区未选择"),
    not_qr(10622,"not qr","未扫码"),
    not_in_working_time(10623,"not in working time","不在工作时间"),

    // 抽奖助手
    not_qr_mp(675,"not qr mp","未扫码"),
    //customize 待分配
    noroleforaccount(104, "User has no role.", "用户没有任何的角色"),
    propertyUsed(105, "This value has been taken.", "该值已被占用"),
    infonotmatch(106, "Information do not match", "信息不匹配"),
    choiceUserError(107, "Please select the user", "请选择用户"),
    fiveresult(108, "已经有五条记录", "已经有五条记录"),
    adminError(304, "permission restrictions!", "超级管理员权限限制"),
    resourceError(305, "No permission", "没有该功能权限"),
    invalidlink(306, "Invalid link", "无效的链接"),
    filetimeout(316, "File was invalid!", "文件已失效"),
    filenoexist(317, "File doesn't exist!", "文件不存在"),
    providerNotExist(501, "Provider does not exist.", "供应商不存在"),
    serviceNotExist(502, "Service does not exist.", "服务不存在"),
    kpiNotExist(503, "KPI does not exist.", "KPI不存在");

    HttpCodeEnum(int code, String msg, String cn_msg) {
        this.code = code;
        this.msg = msg;
        this.cn_msg = cn_msg;
    }

    private int code;
    private String msg;
    private String cn_msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCn_msg() {
        return cn_msg;
    }

    public void setCn_msg(String cn_msg) {
        this.cn_msg = cn_msg;
    }

    public HttpCodeEnum msg(String msg){
        this.msg = msg;
        return this;
    }
    public HttpCodeEnum cn_msg(String cn_msg){
        this.cn_msg = cn_msg;
        return this;
    }

    public HttpCodeEnum code(int code){
        this.code = code;
        return this;
    }
}
