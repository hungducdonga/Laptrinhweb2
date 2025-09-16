package com.example.spring_first_project.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component // Đăng ký lớp này thành Spring Bean để có thể inject ở nơi khác
public class JwtService {

    // Khóa bí mật dùng để ký và xác minh JWT.
    // ⚠️ Thực tế nên lưu trong biến môi trường (ENV) hoặc file config bảo mật.
    public static final String SECRET_KEY =
            "weroiuwroiweurieworuewioruewioruioewrewlkdsjflkjdsfkldsjfklsdf";

    // Thời gian sống của token: 1 giờ (tính bằng milli-giây)
    public static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Tạo token cho user cụ thể.
     * @param userName username của người dùng
     * @param role     vai trò (hiện chưa thêm vào claims)
     * @return JWT string
     */
    public String generateToken(String userName, String role) {
        Map<String, Object> claims = new HashMap<>();
        // Nếu muốn lưu role vào token, có thể mở dòng sau:
        // claims.put("role", role);
        return createToken(claims, userName);
    }

    /**
     * Xây dựng JWT với subject (userName) và các claims tùy chỉnh.
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)                                   // dữ liệu bổ sung
                .setSubject(userName)                                // subject = username
                .setIssuedAt(new Date())                             // thời điểm phát hành
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRATION_TIME))          // hạn token
                .signWith(getSignKey(), SignatureAlgorithm.HS256)    // ký với thuật toán HS256
                .compact();                                           // build thành chuỗi JWT
    }

    /**
     * Lấy SecretKey từ chuỗi base64 SECRET_KEY để ký và xác minh token.
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ========== Các hàm giải mã & kiểm tra token ==========

    /** Lấy username (subject) từ JWT */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Lấy thời điểm hết hạn từ JWT */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Hàm tổng quát để lấy bất kỳ claim nào từ JWT.
     * @param claimsResolver hàm lấy dữ liệu từ đối tượng Claims
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Giải mã token và lấy toàn bộ payload (claims) bên trong.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())  // dùng cùng secret key để xác minh
                .parseClaimsJws(token)
                .getBody();
    }

    /** Kiểm tra token đã hết hạn chưa */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Xác thực token:
     *  - username trong token trùng với userDetails
     *  - token chưa hết hạn
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }
}
