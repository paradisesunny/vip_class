package com.kingyee.prad;

import com.kingyee.prad.wx.mp.MyWeixinHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarterApplicationTests {

    @Autowired
    private MyWeixinHelper myWeixinHelper;
    @Test
    public void contextLoads() {
        try {
//            String codeUrl = myWeixinHelper.buildMeetingQrCode(10L);
            String codeUrl2 = myWeixinHelper.buildMeetingTmpStrQrCode(20L);
            System.out.println(codeUrl2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
