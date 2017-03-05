package com.pine.ant.shiro;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pine.ant.dao.UserDao;
import com.pine.ant.model.Role;
import com.pine.ant.model.User;

public class MyShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    UserDao userDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("执行Shiro权限认证");
        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String)super.getAvailablePrincipal(principalCollection); 
        //到数据库查是否有此对象
        List<User> users=userDao.findUserByName(loginName);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        if(CollectionUtils.isNotEmpty(users)){
            User user=users.get(0);
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色集合
            info.setRoles(user.getRolesName());
            //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            List<Role> roleList=user.getRoleList();
            for (Role role : roleList) {
                info.addStringPermissions(role.getPermissionsName());
            }
            // 或者按下面这样添加
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色    
            // simpleAuthorInfo.addRole("admin");  
            //添加权限  
            //simpleAuthorInfo.addStringPermission("admin:manage");  
            //logger.info("已为用户[mike]赋予了[admin]角色和[admin:manage]权限");
            return info;
        }
            // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 查出是否有此用户
        List<User> users = userDao.findUserByName(token.getUsername());
        if (CollectionUtils.isNotEmpty(users)) {
            User user =users.get(0);
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
        }
        return null;
    }

}
