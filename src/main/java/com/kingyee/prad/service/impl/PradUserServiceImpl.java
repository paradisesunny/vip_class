package com.kingyee.prad.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.http.HttpUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.EmtUtil;
import com.kingyee.prad.common.MedliveUtil;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.mapper.PradUserMapper;
import com.kingyee.prad.service.IPradUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
@Service
public class PradUserServiceImpl extends ServiceImpl<PradUserMapper, PradUser> implements IPradUserService {

	private static final Logger logger = LoggerFactory.getLogger(PradUserServiceImpl.class);

    @Override
    public ExcelData exportUserExcel(List<PradUser> userList) {
        ExcelData data = new ExcelData();
        data.setName("注册用户信息");
        List<String> titles = Arrays.asList("用户id", "用户名", "省份", "城市", "区县", "医院", "科室", "职称", "手机号", "创建时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            for (PradUser user : userList) {
                List<Object> row = new ArrayList<>();
                row.add(user.getPuId());
                row.add(user.getPuName());
                row.add(user.getPuProvince());
                row.add(user.getPuCity());
                row.add(user.getPuRegion());
                row.add(user.getPuHospital());
                row.add(user.getPuDept());
                row.add(user.getPuProfessional());
                row.add(user.getPuPhone());
                row.add(TimeUtil.longToString(user.getPuCreateTime(),"yyyy-MM-dd"));
                rows.add(row);
            }
        }
        data.setRows(rows);
        return data;
    }


	/**
	 * 根据医脉通id获取医脉通用户信息
	 */
	@Override
	public PradUser getUserByMedliveId(Long medUserId) {
		String[] userHashInfo = null;
		String userInfo = null;
		JSONObject json = null;
		// 计算用户加密信息
		userHashInfo = EmtUtil.hashUserInfo(medUserId);
		// fetch userInfo by API
		userInfo = HttpUtil.getMeliveByUserId(String.format(MedliveUtil.GET_USER_INFO, userHashInfo[0], userHashInfo[1]));
		json = JSONObject.parseObject(userInfo);
		boolean success = json.getString("success_msg").equals("success");
		if (success) {
			json = json.getJSONObject("data");
			PradUser user = new PradUser();
			user.setPuMedliveUserId(medUserId);
			String name = json.getString("name");//真实姓名
			if (StringUtils.isEmpty(name)) {
				name = json.getString("nick");//昵称
			}
			user.setPuName(name);
			user.setPuProvince(json.getString("province"));
			user.setPuCity(json.getString("city"));
			user.setPuRegion(json.getString("district"));
			user.setPuPhone(json.getString("mobile"));
			if (StringUtils.isNotEmpty(json.getString("thumb"))) {
				user.setPuMedliveUserThumb(json.getString("thumb"));
				//医脉通用户头像
				String headImgPath = Const.MED_HEADIMG_PATH + medUserId + ".jpeg";
				String path = WebUtil.getRealPath(Const.MED_HEADIMG_PATH);
				try {
					HttpUtil.downloadPicture(json.getString("thumb"), path, medUserId + ".jpeg");
					user.setPuMedliveUserThumb(headImgPath);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("下载医脉通用户头像出错！保存为链接形式", e);
					user.setPuMedliveUserThumb(json.getString("thumb"));
				}
			}
			//职称
			user.setPuProfessional(json.getString("carclass2"));
//            secUser.setUsEmail(json.getString("email"));

			// 医院城市、级别编码
			String company = json.getString("company"); // JSON后无序
			if (!StringUtils.isEmpty(company) && !company.equals("[]")) {

				// 正则获取医院信息
				company = userInfo.replaceAll("\"|\\s", "").replaceAll(".*company:\\{([^\\}]*)\\}.*", "$1");
				String[] infos = company.split(",");
				if (infos.length >= 4) {
					int hospatialCode = Integer.parseInt(infos[3].split(":")[0]);
					String hos = EmtUtil.unicodeToString(infos[3].split(":")[1]);
					user.setPuHospital(hos);
				}
			}
			// 科室编码
			String profession = json.getString("profession"); // JSON后无序
			if (!StringUtils.isEmpty(profession) && !profession.equals("[]")) {
				// 正则获取专业信息
				profession = userInfo.replaceAll("\"|\\s", "").replaceAll(".*profession:\\{([^\\}]*)\\}.*", "$1");
				String[] infos = profession.split(",");
				String ks = EmtUtil.unicodeToString(infos[0].split(":")[1]);
				user.setPuDept(ks);
			}
			user.setPuFrom("医脉通");
			return user;
		}
		return null;
	}
}
