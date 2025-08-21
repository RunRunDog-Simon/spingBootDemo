package com.gtalent.demo.configs;

import com.gtalent.demo.model.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    //驗證流程的核心:基於OncePerRequestFilter自定義的一個過濾器 JwtAuthFilter
    //為何用OncePerRequestFilter??-->確保每個請求都會經過此過濾器一次

    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
//    以下是上面依賴注入的偷懶
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private UserRepository userRepository;


    @Override
    //以下是必須實作繼承類的抽象方法：此方法是過濾器的主要邏輯
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //authHeader -->想像authHeader是MAP，getHeader就是getKey，Key是Authorization，所以對應到Bearer
        //從http headers中獲取Authorization欄位--> bearer......
        String authHeader = request.getHeader("Authorization");
        // 檢查Authorization格式是否正確
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //該次請求過濾器結束生命週期-->將請求往下傳遞
            filterChain.doFilter(request, response);
            return;
        }
        // 若開頭格式(Bearer...)正確，則擷取第七字源開始的字串(實際jwt)
        String jwtToken = authHeader.substring(7);
        try {
            String username = jwtService.getUsernameFromToken(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //db裡面找到對應的username
                Optional<User> user = userRepository.findByUsername(username);
                //todo 驗證token是否過期或無效
                if (user.isPresent()) {
                    //*** 若使用Spring Security (library)必須包含 授權 (Authorization)邏輯->「該用戶能做什麼？」***
//                    List<? extends GrantedAuthority> authorities = getUserAuthorities();
                    List<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.get().getRole()));
                    //該token並非jwt token，而是spring Security內部使用的token(包含user & authorities)
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.get(), null, authorities);
                    //將 Spring Security內部使用的token 投進 Spring Security的 "認證箱"(SecurityContextHolder)
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            //該次請求過濾器結束生命週期-->將請求繼續往下傳遞...
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            System.out.println("你過期了");

        }
    }
    private List<? extends GrantedAuthority> getUserAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
