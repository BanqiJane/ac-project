package xyz.acproject.security_flux.service;


import xyz.acproject.security_flux.entity.UserDetail;

/**
 * @author Jane
 * @ClassName TokenService
 * @Description TODO
 * @date 2021/4/7 12:28
 * @Copyright:2021
 */
public interface TokenService {

    boolean isTokenExpired(String token);

    boolean validateToken(String token,UserDetail userDetail);

    boolean validateToken(String token,Long userId);

    boolean validateToken(String token,String uuid);

    String generateToken(UserDetail userDetail);

    String generateToken(Long userId);

    String generateToken(String uuid);

    String generateToken(UserDetail userDetail,Long expireTime);

    String generateToken(Long userId,Long expireTime);

    String generateToken(String uuid,Long expireTime);

    UserDetail getTokenByUserDetail(String token);

    Long getTokenByLong(String token);

    String getTokenByString(String token);
}
