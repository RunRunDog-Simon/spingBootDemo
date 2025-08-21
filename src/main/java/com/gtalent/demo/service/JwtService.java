package com.gtalent.demo.service;

import com.gtalent.demo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private long EXPIRATION_TIME; //毫秒
        public String generateToken(User user){
            return buildToken(user);
    }

    private String buildToken(User user){
        return Jwts.builder()
                //唯一的使用者
                .setSubject(user.getUsername())
                //發行時間
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //過期時間
                .setExpiration(new Date(System.currentTimeMillis() + 6000000)) //10分鐘
                //對jwt進行簽名
                .signWith(getKey(), SignatureAlgorithm.HS256) //密碼加密
                .compact(); //壓縮
    }

    private Key getKey(){
            byte[] keyByte = Decoders.BASE64.decode("YXJyYW5nZWd1YXJkYXBhcnRmdWxseWxlYWZoYWR0cnVja2VtcHR5aGFwcGlseWJsb2M=");
                    return Keys.hmacShaKeyFor(keyByte);
    }

    public String getUsernameFromToken(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }


}
