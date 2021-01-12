package com.example.config;

import com.example.jwt.CumtomTokenEnhancer;
import com.example.security.CumtomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;


@Configuration
@EnableAuthorizationServer // 激活OAuth2.0 显示的表示是一个授权服务
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private CumtomUserDetailService userDetailsService;


    /**
     * 方法实现说明: 使用jwt存储token,我们创建jwtTOkenStore的时候 需要一个组件
     * jwtAccessTokenConverter  所以我们 可以通过@Bean的形式 创建一个该组件.
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:17
     */
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 这个组件 用于jwt basecode 字符串和 安全认证对象的信息转化
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //jwt的密钥(用来保证jwt 字符串的安全性  jwt可以防止篡改  但是不能防窃听  所以jwt不要 放敏感信息)
        converter.setKeyPair(keyPair());
        //converter.setSigningKey("123456");
        return converter;
    }

    /**
     * KeyPair是 非对称加密的公钥和私钥的保存者
     * @return
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }


    /**
     * 该组件就是用来给jwt令牌中添加额外信息的 来增强我们的jwt的令牌信息
     * @return
     */
    @Bean
    public CumtomTokenEnhancer cumtomTokenEnhancer() {
        return new CumtomTokenEnhancer();
    }



    /**
     * 方法实现说明:认证服务器能够给哪些 客户端颁发token  我们需要把客户端的配置 存储到
     * 数据库中 可以基于内存存储和db存储
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:18
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 使用内存来存储客户端的信息
                .withClient("c1") // 客户端编号
                .secret(new BCryptPasswordEncoder().encode("secret"))
                .resourceIds("res1")//可以访问的资源的编号
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token") //该客户端允许的授权类型
                .scopes("all") // 允许授权的范围  我们对资源操作的作用域 读 写
                .autoApprove(false) // false的话 请求到来的时候会跳转到授权页面
                .redirectUris("http://www.baidu.com") // 回调的地址  授权码会作为参赛绑定在重定向的地址中
        ;
    }


    /**
     * 方法实现说明:授权服务器的配置的配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:21
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        /*
          增加我们的令牌信息
         */
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(cumtomTokenEnhancer(),jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore()) //授权服务器颁发的token 怎么存储的
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService) //用户来获取token的时候需要 进行账号密码
                .authenticationManager(authenticationManager);
    }


    /**
     * 方法实现说明:授权服务器安全配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:23
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //第三方客户端校验token需要带入 clientId 和clientSecret来校验
        security .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");//来获取我们的tokenKey需要带入clientId,clientSecret

        security.allowFormAuthenticationForClients();
    }
}
