package xyz.acproject.security_flux.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import xyz.acproject.cache.RedisService;
import xyz.acproject.security_flux.entity.UserDetail;
import xyz.acproject.security_flux.service.JwtService;
import xyz.acproject.security_flux.service.TokenService;
import xyz.acproject.utils.SpringUtils;
import xyz.acproject.utils.StringsUtils;
import xyz.acproject.utils.random.RandomUtils;
import xyz.acproject.utils.security.BASE64EncoderUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jane
 * @ClassName RedisJwtTokenServiceImpl
 * @Description TODO
 * @date 2021/5/31 11:01
 * @Copyright:2021
 */
public class RedisJwtTokenServiceImpl implements TokenService {

    @Resource
    private JwtService jwtService;
    private RedisService redisService;

    private int max = 20;

    private String prefix = "";

    private Long expire_time = 60 * 60 * 3L;


    @Override
    public boolean isTokenExpired(String token) {
        String hKey = StringUtils.isNotBlank(this.prefix) ?  StringsUtils.appendAll(this.prefix,":",token) : token;
        return redisService.hasKey(hKey);
    }

    @Override
    public boolean validateToken(String token, UserDetail userDetail) {
        String hKey = StringUtils.isNotBlank(this.prefix) ?  StringsUtils.appendAll(this.prefix,":",token) : token;
        if (!redisService.hasKey(hKey)) return false;
        UserDetail userDetailToken = redisService.get(hKey);
        return userDetail.getUsername().equals(userDetailToken.getUsername());
    }

    @Override
    public boolean validateToken(String token, Long userId) {
        String hKey = StringUtils.isNotBlank(this.prefix) ?  StringsUtils.appendAll(this.prefix,":",token) : token;
        if (!redisService.hasKey(hKey)) return false;
        Long userIdToken = redisService.get(hKey);
        return userIdToken == userId.longValue();
    }

    @Override
    public boolean validateToken(String token, String uuid) {
        String hKey = StringUtils.isNotBlank(this.prefix) ?  StringsUtils.appendAll(this.prefix,":",token) : token;
        if (!redisService.hasKey(hKey)) return false;
        String uuidToken = redisService.get(hKey);
        return uuid.equals(uuidToken);
    }

    @Override
    public String generateToken(UserDetail userDetail) {
        String token =jwtService.generateToken(userDetail);
        String hKey = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",userDetail.getUsername(),":",token): token;
        synchronized (userDetail) {
            Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",userDetail.getUsername(),"*"));
            if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
            if (max != 0) {
                if (set.size() >= max) {
                    redisService.deletes(set.stream().toArray()[0]);
                }
                redisService.set(hKey, userDetail, this.expire_time);

            } else {
                redisService.set(hKey, userDetail, this.expire_time);
            }
        }
        return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
    }

    @Override
    public String generateToken(Long userId) {
        UserDetail userDetail = UserDetail.builder()
                .username(userId.toString())
                .password(userId.toString())
                .uuid(userId.toString())
                .buildSelf();
        String token =jwtService.generateToken(userDetail);
        String hKey = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",userId,":",token) : token;
        synchronized (userId) {
            Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",userId,"*"));
            if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
            if (max != 0) {
                if (set.size() >= max) {
                    redisService.deletes(set.stream().toArray()[0]);
                }
                redisService.set(hKey, userId, this.expire_time);
            } else {
                redisService.set(hKey, userId, this.expire_time);
            }
        }
        return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
    }

    @Override
    public String generateToken(String uuid) {
        UserDetail userDetail = UserDetail.builder()
                .username(uuid)
                .password(uuid)
                .uuid(uuid)
                .buildSelf();
        BASE64EncoderUtils base64EncoderUtils = new BASE64EncoderUtils();
        try {
            String u = base64EncoderUtils.encode(uuid.getBytes(StandardCharsets.UTF_8));
            String token = jwtService.generateToken(userDetail)+u;
            String hKey = StringUtils.isNotBlank(this.prefix) ?StringsUtils.appendAll(this.prefix,"_",uuid,":",token) : token;
            synchronized (uuid) {
                Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",uuid,"*"));
                if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
                if (max != 0) {
                    if (set.size() >= max) {
                        redisService.deletes(set.stream().toArray()[0]);
                    }
                    redisService.set(hKey, uuid, this.expire_time);

                } else {
                    redisService.set(hKey, uuid, this.expire_time);
                }
            }
            return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String generateToken(UserDetail userDetail, Long expireTime) {
        String token =jwtService.generateToken(userDetail);
        String hKey = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",userDetail.getUsername(),":",token): token;
        synchronized (userDetail) {
            Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",userDetail.getUsername(),"*"));
            if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
            if (max != 0) {
                if (set.size() >= max) {
                    redisService.deletes(set.stream().toArray()[0]);
                }
                redisService.set(hKey, userDetail, expireTime==null?this.expire_time:expireTime);

            } else {
                redisService.set(hKey, userDetail, expireTime==null?this.expire_time:expireTime);
            }
        }
        return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
    }

    @Override
    public String generateToken(Long userId, Long expireTime) {
        UserDetail userDetail = UserDetail.builder()
                .username(userId.toString())
                .password(userId.toString())
                .uuid(userId.toString())
                .buildSelf();
        String token =jwtService.generateToken(userDetail);
        String hKey = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",userId,":",token) : token;
        synchronized (userId) {
            Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",userId,"*"));
            if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
            if (max != 0) {
                if (set.size() >= max) {
                    redisService.deletes(set.stream().toArray()[0]);
                }
                redisService.set(hKey, userId, expireTime==null?this.expire_time:expireTime);
            } else {
                redisService.set(hKey, userId, expireTime==null?this.expire_time:expireTime);
            }
        }
        return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
    }

    @Override
    public String generateToken(String uuid, Long expireTime) {
        UserDetail userDetail = UserDetail.builder()
                .username(uuid)
                .password(uuid)
                .uuid(uuid)
                .buildSelf();
        BASE64EncoderUtils base64EncoderUtils = new BASE64EncoderUtils();
        try {
            String u = base64EncoderUtils.encode(uuid.getBytes(StandardCharsets.UTF_8));
            String token = jwtService.generateToken(userDetail)+u;
            String hKey = StringUtils.isNotBlank(this.prefix) ?StringsUtils.appendAll(this.prefix,"_",uuid,":",token) : token;
            synchronized (uuid) {
                Set<String> set = redisService.scan(StringsUtils.appendAll(this.prefix,"_",uuid,"*"));
                if (CollectionUtils.isEmpty(set)) set = new HashSet<>();
                if (max != 0) {
                    if (set.size() >= max) {
                        redisService.deletes(set.stream().toArray()[0]);
                    }
                    redisService.set(hKey, uuid, expireTime==null?this.expire_time:expireTime);

                } else {
                    redisService.set(hKey, uuid, expireTime==null?this.expire_time:expireTime);
                }
            }
            return StringUtils.isNotBlank(this.prefix) ? parse(hKey) : token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public UserDetail getTokenByUserDetail(String token) {
        BASE64EncoderUtils base64EncoderUtils = new BASE64EncoderUtils();
        try {
            String tokenP  = new String(base64EncoderUtils.decode(token.substring(40)),"utf-8");
            token = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",tokenP,":",token) : token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return redisService.get(token);
    }

    @Override
    public Long getTokenByLong(String token) {
        BASE64EncoderUtils base64EncoderUtils = new BASE64EncoderUtils();
        try {
            String tokenP  = new String(base64EncoderUtils.decode(token.substring(40)),"utf-8");
            token = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",tokenP,":",token) : token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return redisService.get(token);
    }

    @Override
    public String getTokenByString(String token) {
        BASE64EncoderUtils base64EncoderUtils = new BASE64EncoderUtils();
        try {
            String tokenP  = new String(base64EncoderUtils.decode(token.substring(40)),"utf-8");
            token = StringUtils.isNotBlank(this.prefix) ? StringsUtils.appendAll(this.prefix,"_",tokenP,":",token) : token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return redisService.get(token);
    }

    private String parse(String token) {
        if (token.indexOf(":") != -1) {
            return token.split(":")[1];
        }
        return token;
    }


    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

    public static int getExpiredTime() {
        String key = SpringUtils.getConfigByKey("acproject.security.expire");
        if (StringUtils.isBlank(key)) {
            return 60 * 60 * 3; // 默认有效期三小时
        }
        return Integer.parseInt(key);
    }
}
