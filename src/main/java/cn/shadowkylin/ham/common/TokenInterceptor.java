package cn.shadowkylin.ham.common;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 请求处理前的预处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            int userId = jwtTokenUtil.getUserIdFromToken(token);
            Object redis_token = redisUtil.get("token-" + userId);
            if (jwtTokenUtil.isTokenExpired(token) || redis_token == null || !token.equals(redis_token.toString())) {
                responseJson(response, ResultUtil.error("登录超时，请重新登录！", HttpStatus.LOGIN_EXPIRE));
                return false;
            } else {
                redisUtil.expire("token-" + userId, 30 * 60);
                return true;
            }
        } else {
            responseJson(response, ResultUtil.error("未登录，请登录！", HttpStatus.NOT_LOGIN));
            return false;
        }
    }

    private void responseJson(HttpServletResponse response, Object result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        //获取PrintWriter输出流
        PrintWriter out = response.getWriter();
        out.write(new Gson().toJson(result));
        out.flush();
        out.close();
    }
}
