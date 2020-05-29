package com.qf.fronted.servlet;

import com.qf.admin.domain.User;
import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.service.UserService;
import com.qf.fronted.service.impl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 注册
 */
@WebServlet(value = "/register", name = "FrontedRegisterServlet")
public class RegisterServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // username=yuyy&password=1&email=7yty%40qq.com&gender=F&phone=87
        String username = req.getParameter("username");

        // 注册的时候转md5
        String password = DigestUtils.md5Hex(req.getParameter("password"));

        String email = req.getParameter("email");
        String gender = req.getParameter("gender");

        String phone = req.getParameter("phone");

        Data data = null;

        try {
            // 返回值为当前插入的数据，数据库自动为我们生成的主键
            Integer pkValue = userService.register(phone, username, password, email, gender);
            //注册成功也要设到session中
            HttpSession session = req.getSession(); //

            User user = new User();   //
            user.setId(pkValue);
            user.setName(username);
            user.setEmail(email);
            user.setPhone(phone);
            user.setSex(gender);

            session.setAttribute("user", user);

            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "注册失败");
        }

        sendJson(resp, data);
    }
}
