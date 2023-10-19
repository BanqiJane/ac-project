package xyz.acproject.security_demo.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.HexUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.gdcx.GdcxClient;
import com.gdcx.dto.MsgListParamDto;
import com.gdcx.dto.MsgParamDto;
import com.gdcx.dto.MsgSendDto;
import com.gdcx.exception.ClientException;
import com.gdcx.vo.SendMsgVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.acproject.cache.annotation.RateLimit;
import xyz.acproject.email.EmailComponent;
import xyz.acproject.lang.exception.ServerException;
import xyz.acproject.lang.exception.ServiceException;
import xyz.acproject.lang.page.Page;
import xyz.acproject.lang.page.PageBean;
import xyz.acproject.lang.params.QueryPageParam;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router.controller.Controller;
import xyz.acproject.security_demo.dto.WaterControlParamDto;
import xyz.acproject.security_demo.entity.Student;
import xyz.acproject.security_demo.excel.UserExcel;
import xyz.acproject.security_demo.excel.listener.UserListener;
import xyz.acproject.security_demo.http.WaterHttp;
import xyz.acproject.security_demo.utils.ExportExcelUtil;
import xyz.acproject.utils.SpringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName TestController
 * @Description TODO
 * @date 2021/3/29 15:34
 * @Copyright:2021
 */
@RestController
@RequestMapping("/test")
public class TestController extends Controller {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private EmailComponent emailComponent;

    RRateLimiter rRateLimiter;



    @Resource
    private GdcxClient gdcxClient;

    @PostConstruct
    public void init() {
        RRateLimiter ra =  redissonClient.getRateLimiter("myRateLimiter");
        ra.trySetRate(RateType.OVERALL,10000,5, RateIntervalUnit.SECONDS);
        rRateLimiter = ra;
    }


    @GetMapping("/test1")
    @RolesAllowed({"ADMIN"})
    public Response<?> method1(){
        return new Response().add("bean","method1").success();
    }

    @GetMapping("/test2")
    @RolesAllowed({"WX"})
    public Response<?> method2(){
        return new Response().add("bean","method2").success();
    }

    @GetMapping("/test3")
    public Response<?> method3(String id) {
        if(StringUtils.isNotBlank(id)){
            Response.data("id",id);
        }
        Integer nextInt =  RandomUtils.nextInt();
        System.err.println(nextInt);
        return new Response<>().add("bean","method3").success().add("roles"+nextInt,"ADMIN,WX");
    }

    @GetMapping("/test4")
//    @RateLimit(permits = 10,timeout = 200)
    public Response<?> method4(){
        boolean b = rRateLimiter.tryAcquire();
        if(!b){
            throw new ServiceException("busy");
        }
        return new Response<>().add("bean","method4").add("data1","data1").add("data2","data2").success();
    }
    @GetMapping("/test5")
    @RateLimit(key="rateTest1",count = 100,time = 3)
    public Response<?> method5(){
        return new Response<>().add("bean","method5").add("data1","data1").add("data2","data2").success();
    }

    @GetMapping("/test6")
    public Response<?> method6(QueryPageParam param){
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1"));
        studentList.add(new Student("2"));
        studentList.add(new Student("3"));
        studentList.add(new Student("4"));
        studentList.add(new Student("5"));
        studentList.add(new Student("6"));
        studentList.add(new Student("7"));
        studentList.add(new Student("8"));
        studentList.add(new Student("9"));
        studentList.add(new Student("10"));
        Page page = new Page(param.getPageNow(),studentList.size());
        page.setPageSize(param.getPageSize());
        page(PageBean.build().list(studentList.subList(page.getListFromIndex(),page.getListToIndex())).page(page));
        return new Response<>().success();
    }

    @GetMapping("/downloadUserDemoExcel")
    public void downloadUserDemoExcel(HttpServletResponse response){
        List<UserExcel> userExcels = new ArrayList<>();
        ExportExcelUtil.export(response,UserExcel.class,userExcels,"用户导入模板");
    }


    @PostMapping("/importUserExcel")
    public Response<?> importUserExcel(@RequestParam(value = "file") @RequestPart("file") MultipartFile file) throws IOException {
        UserListener userListener = SpringUtils.getBean(UserListener.class);
        EasyExcel.read(file.getInputStream(), UserExcel.class, userListener).sheet(0).doRead();
        return new Response<>().success();
    }

    @PostMapping("/test6")
    public Response<?> method6(Long val){
        return new Response<>().success().data(Convert.toStr(val));
    }

    @PostMapping("/test7")
    public Response<?> method7(){
        emailComponent.sendSimpleMail("测试邮件","这是一个测试邮件，切勿回复","zjianf2014@outlook.com");
        return new Response<>().data("aaaa").success();
    }
    @PostMapping("/test8")
    public Response<?> method8() throws ClientException {
        MsgSendDto msgSendDto = new MsgSendDto();
        msgSendDto.setTemplateId(1688757106428080129l);
        List<MsgListParamDto> msgListParamDtoList = new ArrayList<>();
        MsgListParamDto msgListParamDto = new MsgListParamDto();
        msgListParamDto.setPhone("13725910531");
        List<MsgParamDto> msgParamDtoList = new ArrayList<>();
        MsgParamDto msgParamDto = new MsgParamDto();
        msgParamDto.setParamKey("code");
        msgParamDto.setParamValue("123456");
        msgParamDtoList.add(msgParamDto);
        msgListParamDto.setMsgParamDtoList(msgParamDtoList);
        msgListParamDtoList.add(msgListParamDto);
        msgSendDto.setMsgListParamDtoList(msgListParamDtoList);
        msgSendDto.setApiKey("1688451780012253184");
        SendMsgVo sendMsgVo = gdcxClient.sendMsg(msgSendDto);
        return new Response<>().data(sendMsgVo).success();
    }


}
