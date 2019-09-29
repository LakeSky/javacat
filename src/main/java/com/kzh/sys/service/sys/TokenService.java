package com.kzh.sys.service.sys;

import com.kzh.sys.dao.TokenDao;
import com.kzh.sys.enums.Platform;
import com.kzh.sys.model.Token;
import com.kzh.sys.util.CollectionUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gang on 2016/8/2.
 */
@Service
@Transactional
public class TokenService {
    public static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    @Resource
    private TokenDao tokenDao;

    public static String secretKey = "itsanewworld";
    public final static long keepTime = 24 * 60 * 60 * 1000;

    public static String genToken(String id, String issuer, String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now);
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (issuer != null) {
            builder.setIssuer(issuer);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        long expMillis = nowMillis + keepTime;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        return builder.compact();
    }

    public String updateToken(String token) {
        try {
            Claims claims = verifyToken(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            String issuer = claims.getIssuer();
            Date date = claims.getExpiration();
            return genToken(id, issuer, subject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";
    }
    
    public String updateTokenBase64Code(String token) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            token = new String(decoder.decodeBuffer(token), "utf-8");
            Claims claims = verifyToken(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            String issuer = claims.getIssuer();
            Date date = claims.getExpiration();
            String newToken = genToken(id, issuer, subject);
            return base64Encoder.encode(newToken.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";
    }
    
    public static Claims verifyToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        return claims;
    }

    //获取token
    public String getToken(String userId, Platform platform) {
        String tokenKey = userId + ";" + platform.name();
        String tokenValue = genToken(tokenKey, platform.name(), null);
        Token token = tokenDao.findByTokenKey(tokenKey);
        if (token == null) {
            token = new Token();
        }
        token.setTokenValue(tokenValue);
        token.setTokenKey(tokenKey);
        token.setIssuer(platform.name());

        tokenDao.save(token);
        tokenMap.put(tokenKey, tokenValue);
        return tokenValue;
    }

    public void saveTokenLog(String userId, String token) {
        Token sysToken = tokenDao.findByTokenKey(userId);
        if (sysToken != null) {
            sysToken.setTokenValue(token);
        } else {
            sysToken = new Token();
            sysToken.setTokenKey(userId);
            sysToken.setTokenValue(token);
        }
        tokenDao.save(sysToken);
    }

    public List<Token> findAll() {
        return tokenDao.findAll();
    }

    public void initCache() {
        List<Token> tokens = tokenDao.findAll();
        if (CollectionUtil.isNotEmpty(tokens)) {
            for (Token token : tokens) {
                tokenMap.put(token.getTokenKey(), token.getTokenValue());
            }
        }
    }

}
