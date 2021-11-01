package com.frank.springboot.web;

import com.frank.springboot.model.Student;
import com.frank.springboot.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class StudentController {

    @Reference(interfaceClass = StudentService.class,version = "1.0.0",check = false)
    private StudentService studentService;

    @RequestMapping(value = "/student/detail/{id}")
    public String studentDetail(Model model, @PathVariable("id") Integer id){
        //根据学生id查询详情
        log.debug("服务消费者，根据学生id查询详情，日志系统debug测试");
        log.error("服务消费者，根据学生id查询详情，日志系统error测试");
        Student student = studentService.queryStudentById(id);
        model.addAttribute("student",student);

        studentService.putRedis(id,model);
        log.warn("值已存入redis。。。");
        return "studentDetail"; //返回studentDetail视图页面
    }

    //从redis取数据，并设置缓存时间，如果redis没有，则从mysql查询并再存入redis
    @RequestMapping(value = "/student/redis/{id}")
    public String redisStudent(Model model, @PathVariable("id") Integer id){
        Student student = studentService.queryStudentByRedis(id);
        model.addAttribute("student",student);
        log.info("消费端：根据id查询学生详情。。。");
        return "studentDetail";
    }

    //这里先直接用json返回测试，不调用前端模板
    @RequestMapping(value = "/student/all/count")
    public @ResponseBody Object allStudentCount(){
        Integer allStudentCount = studentService.queryStudentCount();
        log.info("消费端：查询学生总人数。。。");
        return "学生总人数：" + allStudentCount;
    }
}
