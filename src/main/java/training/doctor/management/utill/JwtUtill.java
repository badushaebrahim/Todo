package training.doctor.management.utill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Slf4j
@Service
public class JwtUtill {


    private static final String SECRET_KEY = "secret";

    public  String createToken(String subject, String issuer, long expirationMillis) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public  Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token){
        Claims claim = parseToken(token);
        Date  now = new Date();
        return  now.before(claim.getExpiration());
    }



    public String getTokenValue(String token){
        if (isValidToken(token)){
           return parseToken(token).getSubject();
        }

        else {return "USER_ID_NOT_VALID";}
    }

}
