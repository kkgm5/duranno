package hi.duranno.web.interceptor;

import hi.duranno.domain.RoleType;
import hi.duranno.web.SessionConst;
import hi.duranno.web.session.SessionValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();

        //로그인 확인
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            if (requestURI.contains("/boards")) {
                response.sendRedirect("/login?redirectURL=/");
                return false;
            }

            if (requestURI.contains("/assignment/") || requestURI.contains("/textbook/")) {
                response.sendRedirect("/login?redirectURL=/archive");
                return false;
            }

            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        SessionValue sessionValue = (SessionValue) session.getAttribute(SessionConst.LOGIN_MEMBER);


        //일반 사용자(USER) 공지사항, 과제, 교재 등록, 수정, 삭제 URL 접근 제어
        if (sessionValue != null) {

            if (sessionValue.getRoleType() == RoleType.USER && requestURI.contains("assignment/add")) {
                response.sendRedirect("/assignment");
                return false;
            }

            if (sessionValue.getRoleType() == RoleType.USER && requestURI.contains("textbook/add")) {
                response.sendRedirect("/textbook");
                return false;
            }

            if (sessionValue.getRoleType() == RoleType.USER && requestURI.contains("boards")) {
                if (requestURI.contains("edit") || requestURI.contains("remove")) {
                    response.sendRedirect("/#reviews");
                    return false;
                }
            }

            if (sessionValue.getRoleType() == RoleType.USER && requestURI.contains("assignment")) {
                if (requestURI.contains("edit") || requestURI.contains("remove")) {
                    response.sendRedirect("/assignment");
                    return false;
                }
            }

            if (sessionValue.getRoleType() == RoleType.USER && requestURI.contains("textbook")) {
                if (requestURI.contains("edit") || requestURI.contains("remove")) {
                    response.sendRedirect("/textbook");
                    return false;
                }
            }
        }

        return true;
    }
}
