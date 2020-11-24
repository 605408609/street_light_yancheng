package cn.test;

import com.exc.street.light.electricity.ElectricityApplication;
import com.exc.street.light.electricity.util.ConstantUtil;
import com.exc.street.light.electricity.util.HexUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Xiaok
 * @Date: 2020/10/16 16:20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ElectricityApplication.class)
@RunWith(SpringRunner.class)
public class Test1 {

    @Test
    public void test1(){
        System.out.println("1 = " + ConstantUtil.REDIS_NODE_KEY);
    }

    @Test
    public void test2(){
        String[] times = "12:01:02".split(":");
        byte[] time = new byte[3];
        time[0] = HexUtil.getByte(Integer.parseInt(times[0]));
        time[1] = HexUtil.getByte(Integer.parseInt(times[1]));
        time[2] = HexUtil.getByte(Integer.parseInt(times[2]));
        String s = HexUtil.bytesTohex(time);
        System.out.println(s);
    }
}
