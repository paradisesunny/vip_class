package com.kingyee.vipclass.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.security.UserUtil;
import com.kingyee.vipclass.entity.PradBookmarkRecord;
import com.kingyee.vipclass.entity.PradNews;
import com.kingyee.vipclass.entity.PradUser;
import com.kingyee.vipclass.mapper.PradNewsMapper;
import com.kingyee.vipclass.service.impl.PradBookmarkRecordServiceImpl;
import com.kingyee.vipclass.service.impl.PradNewsServiceImpl;
import com.kingyee.vipclass.service.impl.PradUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/userCenter")
public class UserCenterController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserCenterController.class);

    @Autowired
    private PradUserServiceImpl userService;
    @Autowired
    private PradNewsServiceImpl newsService;
    @Autowired
    private PradBookmarkRecordServiceImpl bookmarkRecordService;
    @Resource
    private PradNewsMapper newsMapper;

    /**
     * 个人中心
     * @return
     */
    @RequestMapping("home")
    public String home(ModelMap mm) {
		PradUser user = userService.getById(UserUtil.getUserId());
        mm.addAttribute("user", user);
		return "web/user/home";
    }

    /**
     * 个人中心——个人信息
     * @return
     */
    @RequestMapping("getUserInfo")
    public String getUserInfo(ModelMap mm) {
        PradUser user = userService.getById(UserUtil.getUserId());
        mm.addAttribute("user", user);
        return "web/user/userInfo";
    }

    /**
     * 转向编辑用户页面
     *
     * @param mm
     * @return
     */
    @RequestMapping("edit")
    public String edit(ModelMap mm) {
        Long id = UserUtil.getUserId();
        PradUser user = userService.getById(id);
        if (user != null) {
            mm.addAttribute("regUser", user);
        }
        if(user.getPuMedliveUserId() != null){
			return "web/user/userInfo4Medlive";
		}
        return "web/user/edit";
    }


    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequestMapping("updateUserInfo")
    public String updateUserInfo(PradUser user) {
        try {
            user.setPuUpdateTime(TimeUtil.dateTolong());
            userService.updateById(user);
            UserUtil.updateSession(user);
            return "redirect:getUserInfo";
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("注册用户编辑出错", e);
            return "error/error";
        }
    }

    /**
     * 收藏
     *
     * @return
     */
    @RequestMapping("collect")
    public String collect(ModelMap mm) {
        Long id = UserUtil.getUserId();
        try {
            QueryWrapper<PradBookmarkRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradBookmarkRecord::getBrUserId, id);
            List<PradBookmarkRecord> bookList = bookmarkRecordService.list(queryWrapper);
            List<PradNews> collectList = new ArrayList<>();
            PradNews existNew = new PradNews();
            for (PradBookmarkRecord i : bookList) {
                if (null != i.getBrNewsId()) {
                    existNew = newsService.getById(i.getBrNewsId());
                    if (existNew != null) {
                        collectList.add(existNew);
                    }
                }
            }
            mm.addAttribute("collectList", collectList);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取收藏列表失败", e);
        }
        return "web/user/collect";
    }
}
