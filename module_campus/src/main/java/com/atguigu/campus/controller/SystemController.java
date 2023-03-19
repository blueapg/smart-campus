package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Admin;
import com.atguigu.campus.pojo.LoginForm;
import com.atguigu.campus.pojo.Student;
import com.atguigu.campus.pojo.Teacher;
import com.atguigu.campus.service.AdminService;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Linda
 * @version 1.0
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    /**
     * 解析 浏览器发送来的请求头中的token
     *
     * @param token 浏览器发送来的token
     * @return 封装数据到Result类 响应给浏览器
     */
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        //首先判断token是否还有效  是不是过期了
        if (JwtHelper.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出 用户类型、用户id
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);

        //准备一个map存放用户响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("user", admin);
                map.put("userType", userType);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("user", student);
                map.put("userType", userType);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("user", teacher);
                map.put("userType", userType);
                break;
        }
        return Result.ok(map);
    }

    /**
     * 登录:进行验证码以及用户输入的账号密码进行校验
     *
     * @return 将校验的结果数据封装到Result类中返回给浏览器 若用户登录成功 则根据id和用户类型生成一个token放在Result类一起返回给浏览器
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码有误，请重新输入");
        }
        //从session域中移除验证码
        session.removeAttribute("verifiCode");

        //分用户类型进行校验

        //准备一个map存放用户响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        //用户的类型和用户的id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        //用户的类型和用户的id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        //用户的类型和用户的id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        //过关斩将验证 查无此用户  返回信息
        return Result.fail().message("查无此用户");
    }

    /**
     * 获取验证码图片响应到浏览器,并将验证码中的值保存到session域中 用于用户登录时/login校验
     */
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放到session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        //获取上传的文件的名称
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        //获得文件的格式 eg： .jpg .jpeg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //避免文件重名  使用UUID随机生成
        String fileName = UUID.randomUUID().toString().toLowerCase().replace("-", "").concat(suffix);
        //保存路径
        String destPath = "D:/idea_java_projects/smart-campus/module_campus/src/main/resources/static/upload/".concat(fileName);
        //文件保存到服务端
        multipartFile.transferTo(new File(destPath));
        return Result.ok("upload/".concat(fileName));
    }

    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@PathVariable String oldPwd,
                            @PathVariable String newPwd,
                            @RequestHeader String token) {
        //1.验证token是否有效
        if (JwtHelper.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //2.解析token获取用户id 根据用户id、验证原密码输入是否正确
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        assert userType != null;
        if (userType == 1) {
            Admin admin = adminService.getAdminById(userId);
            if (!MD5.encrypt(oldPwd).equals(admin.getPassword())) {
                return Result.fail().message("原密码输入有误，请重新输入");
            }
            admin.setPassword(MD5.encrypt(newPwd));
            adminService.saveOrUpdate(admin);
        } else if (userType == 2) {
            Student student = studentService.getStudentById(userId);
            if (!MD5.encrypt(oldPwd).equals(student.getPassword())) {
                return Result.fail().message("原密码输入有误，请重新输入");
            }
            student.setPassword(MD5.encrypt(newPwd));
            studentService.saveOrUpdate(student);
        } else {
            Teacher teacher = teacherService.getTeacherById(userId);
            if (!MD5.encrypt(oldPwd).equals(teacher.getPassword())) {
                return Result.fail().message("原密码输入有误，请重新输入");
            }
            teacher.setPassword(MD5.encrypt(newPwd));
            teacherService.saveOrUpdate(teacher);
        }
        //3.修改新密码
        return Result.ok();
    }

}
