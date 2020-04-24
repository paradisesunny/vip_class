package com.kingyee.prad.controller.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.NewsCategoryConstant;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.PradBookmarkRecord;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.mapper.PradNewsMapper;
import com.kingyee.prad.model.NewsModel;
import com.kingyee.prad.service.impl.PradBookmarkRecordServiceImpl;
import com.kingyee.prad.service.impl.PradNewsServiceImpl;
import com.kingyee.prad.service.impl.PradUserServiceImpl;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat/userCenter")
public class UserCenterController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserCenterController.class);

    @Autowired
    private PradUserServiceImpl userService;
    @Autowired
    private WechatUserServiceImpl wechatUserService;
    @Autowired
    private PradNewsServiceImpl newsService;
    @Autowired
    private PradBookmarkRecordServiceImpl bookmarkRecordService;
    @Resource
    private PradNewsMapper newsMapper;

    @RequestMapping("center")
    public String userCenter(ModelMap mm) {
//            取头像
		mm.addAttribute("headImg", UserUtil.getHeadImg());
//            用户名
		mm.addAttribute("name", UserUtil.getUserName());
		//        浏览课程列表
		List<NewsModel> courseList = newsMapper.getViewedCourseList(UserUtil.getUserId());
		Map<Long, Integer> sumMap = new HashMap<>();
		List<NewsModel> list = newsMapper.getPlaybackFolder(NewsCategoryConstant.DOCTOR_RESOURCES);
		for (NewsModel i : list) {
			sumMap.put(i.getFileId(), i.getNewsNum());
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
//        保留几位小数
		numberFormat.setMaximumFractionDigits(0);
		if (courseList != null && courseList.size() > 0 && list != null && list.size() > 0) {
			for (NewsModel record : courseList) {
				if (null != record.getFileId()) {
					String result = numberFormat.format((float) record.getNewsNum() / (float) sumMap.get(record.getFileId()) * 100) + "%";
					record.setPercent(result);
				}
			}
		}
		mm.addAttribute("courseList", courseList);
		PradUser user = userService.getById(UserUtil.getUserId());
        mm.addAttribute("user", user);
        mm.addAttribute("courseList", courseList);
		return "wechat/user/center";
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
			return "wechat/user/userInfo4Medlive";
		}
        return "wechat/user/edit";
    }


//    /**
//     * 修改用户
//     *
//     * @param user
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("update")
//    public JsonNode updateAjax(PradUser user) {
//        try {
//            user.setPuUpdateTime(TimeUtil.dateTolong());
//            userService.updateById(user);
//            return JacksonMapper.newSuccessInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JacksonMapper.newErrorInstance("注册用户编辑出错");
//        }
//    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @RequestMapping("update")
    public String update(PradUser user) {
        try {
            user.setPuUpdateTime(TimeUtil.dateTolong());
            userService.updateById(user);
            UserUtil.updateSession(user);
            return "redirect:center";
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
        return "wechat/user/collect";
    }
}
