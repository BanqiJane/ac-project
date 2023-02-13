package xyz.acproject.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import xyz.acproject.utils.JodaTimeUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * @author Jane
 * @ClassName UserReDetail
 * @Description TODO
 * @date 2021/2/18 16:52
 * @Copyright:2021
 */
public class UserDetail implements UserDetails {


    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String password;

    private String uuid;

    private Long createTime;

    private String username;

    private Integer level;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private Set<GrantedAuthority> grantedAuthorities;

    public UserDetail(String username, String password, Collection<GrantedAuthority> grantedAuthorities) {
        this(null,username,password,System.currentTimeMillis(),true,true,true,true,grantedAuthorities);
    }

    public UserDetail(String uuid, String username, String password, Collection<GrantedAuthority> grantedAuthorities) {
        this(uuid,username,password,System.currentTimeMillis(),true,true,true,true,grantedAuthorities);
    }

    public UserDetail(String uuid, String username, Integer level, String password, Collection<GrantedAuthority> grantedAuthorities) {
        this(uuid,username,password,level,System.currentTimeMillis(),true,true,true,true,grantedAuthorities);
    }

    public UserDetail(String uuid, String username, String password, Long createTime, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Collection<GrantedAuthority> grantedAuthorities) {
        this.password = password;
        this.uuid = uuid;
        this.createTime = createTime;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.grantedAuthorities = sortAuthorities(grantedAuthorities);
    }

    public UserDetail(String uuid, String username, String password, Integer level, Long createTime, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Collection<GrantedAuthority> grantedAuthorities) {
        this.password = password;
        this.uuid = uuid;
        this.createTime = createTime;
        this.username = username;
        this.level=level;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.grantedAuthorities = sortAuthorities(grantedAuthorities);
    }

    public static SortedSet<GrantedAuthority> sortAuthorities(Collection<GrantedAuthority> grantedAuthorities) {
//        Assert.notNull(grantedAuthorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    public String getUuid(){
        return this.uuid;
    }

    public Long getCreateTime(){
        return this.createTime;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDetail) {
            return this.username.equals(((UserDetail) obj).username);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" [");
        sb.append("uuid=").append(this.uuid).append(", ");
        sb.append("Username=").append(this.username).append(", ");
        sb.append("Password=[PROTECTED], ");
        sb.append("Level=").append(this.level).append(", ");
        sb.append("createTime=").append(this.createTime!=null? JodaTimeUtils.formatDateTime(this.createTime):null).append(", ");
        sb.append("Enabled=").append(this.enabled).append(", ");
        sb.append("AccountNonExpired=").append(this.accountNonExpired).append(", ");
        sb.append("credentialsNonExpired=").append(this.credentialsNonExpired).append(", ");
        sb.append("AccountNonLocked=").append(this.accountNonLocked).append(", ");
        sb.append("Granted Authorities=").append(this.grantedAuthorities).append("]");
        return sb.toString();
    }

    public static UserDetailBuilder withUsername(String username) {
        return builder().username(username);
    }

    public static UserDetailBuilder withUuid(String uuid) {
        return builder().uuid(uuid);
    }

    public static UserDetailBuilder builder() {
        return new UserDetailBuilder();
    }

    @Deprecated
    public static UserDetailBuilder withDefaultPasswordEncoder() {
//        logger.warn("User.withDefaultPasswordEncoder() is considered unsafe for production "
//                + "and is only intended for sample applications.");
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return builder().passwordEncoder(encoder::encode);
    }

    public static UserDetailBuilder withUserDetails(UserDetails userDetails) {
        // @formatter:off
        return withUsername(userDetails.getUsername())
                .password(userDetails.getPassword())
                .accountExpired(!userDetails.isAccountNonExpired())
                .accountLocked(!userDetails.isAccountNonLocked())
                .authorities(userDetails.getAuthorities())
                .credentialsExpired(!userDetails.isCredentialsNonExpired())
                .enable(!userDetails.isEnabled());
        // @formatter:on
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set. If the authority is null, it is a custom authority and should
            // precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
    public static final class UserDetailBuilder{
        private String username;

        private String password;

        private List<GrantedAuthority> authorities;

        private boolean accountExpired;

        private boolean accountLocked;

        private boolean credentialsExpired;

        private boolean enable;

        private String uuid;

        private Integer level;

        private Long createTime;

        private Function<String, String> passwordEncoder = (password) -> password;

        private UserDetailBuilder() {
        }

        public UserDetailBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetailBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDetailBuilder passwordEncoder(Function<String, String> passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public UserDetailBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities(authorities);
        }

        public UserDetailBuilder roles(Collection<? extends GrantedAuthority> authorities) {
            this.authorities.addAll(authorities);
            return this;
        }

        public UserDetailBuilder roles(List<String> roles) {
            if(CollectionUtils.isEmpty(roles))return this;
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities(authorities);
        }

        public UserDetailBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }

        public UserDetailBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public UserDetailBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public UserDetailBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public UserDetailBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public UserDetailBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public UserDetailBuilder enable(boolean enable) {
            this.enable = enable;
            return this;
        }

        public UserDetailBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public UserDetailBuilder level(Integer level) {
            this.level = level;
            return this;
        }

        public UserDetailBuilder createTime(Long createTime) {
            this.createTime = createTime;
            return this;
        }
        public UserDetails build(){
            String passwordEncoder = this.passwordEncoder.apply(this.password);
            return new UserDetail(this.uuid,this.username,passwordEncoder,this.level,System.currentTimeMillis(),!this.accountExpired,!this.accountLocked,!this.credentialsExpired,!this.enable,this.authorities);
        }
        public UserDetail buildSelf(){
            String passwordEncoder = this.passwordEncoder.apply(this.password);
            return new UserDetail(this.uuid,this.username,passwordEncoder,this.level,System.currentTimeMillis(),!this.accountExpired,!this.accountLocked,!this.credentialsExpired,!this.enable,this.authorities);
        }
    }
}
