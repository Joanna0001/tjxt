package com.tianji.auth.controller;


import com.tianji.api.dto.user.LoginFormDTO;
import com.tianji.auth.common.constants.JwtConstants;
import com.tianji.auth.service.IAccountService;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 账户登录相关接口
 */
@RestController
@RequestMapping("/accounts")
@Api(tags = "账户管理")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final IAccountService accountService;

    @ApiOperation("登录并获取token")
    @PostMapping(value = "/login")
    public String loginByPw(@RequestBody LoginFormDTO loginFormDTO) {
        return accountService.login(loginFormDTO, false);
    }

    @ApiOperation("管理端登录并获取token")
    @PostMapping(value = "/admin/login")
    public String adminLoginByPw(@RequestBody LoginFormDTO loginFormDTO) {
        return accountService.login(loginFormDTO, true);
    }

    @ApiOperation("退出登录")
    @PostMapping(value = "/logout")
    public void logout() {
        accountService.logout();
    }

    @ApiOperation("刷新token")
    @GetMapping(value = "/refresh")
    public String refreshToken(
            @CookieValue(value = JwtConstants.REFRESH_HEADER, required = false) String studentToken,
            @CookieValue(value = JwtConstants.ADMIN_REFRESH_HEADER, required = false) String adminToken
    ) {
        log.info("refreshToken: studentToken={}, adminToken={}", studentToken, adminToken);
        if (StringUtils.isBlank(studentToken) && StringUtils.isBlank(adminToken)) {
            throw new BadRequestException("登录超时");
        }
        // 优先使用studentToken
        if(StringUtils.isNotBlank(studentToken)){
            try {
                return accountService.refreshToken(WebUtils.cookieBuilder().decode(studentToken));
            } catch (Exception e) {
                log.debug("student refresh token failed", e);
                // 可能是token过期或无效，尝试 admin-token
                if(StringUtils.isBlank(adminToken)){
                    // 没有admin-token，则直接抛出异常
                    throw e;
                }
            }
        }
        // student-token 刷新失败，或者 student-token为null，尝试admin-token
        if (StringUtils.isNotBlank(adminToken)) {
            return accountService.refreshToken(WebUtils.cookieBuilder().decode(adminToken));
        }
        throw new BadRequestException("登录超时");
    }
}
