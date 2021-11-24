package cn.iocoder.yudao.framework.pay.core.client.impl.wx;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.pay.core.client.PayClientConfig;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

// TODO 芋艿：参数校验

/**
 * 微信支付的 PayClientConfig 实现类
 * 属性主要来自 {@link com.github.binarywang.wxpay.config.WxPayConfig} 的必要属性
 *
 * @author 芋道源码
 */
@Data
public class WXPayClientConfig implements PayClientConfig {

    // TODO 芋艿：V2 or V3 客户端
    /**
     * API 版本 - V2
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_1
     */
    public static final String API_VERSION_V2 = "v2";
    /**
     * API 版本 - V3
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay-1.shtml
     */
    public static final String API_VERSION_V3 = "v3";

    /**
     * 公众号或者小程序的 appid
     */
    @NotBlank(message = "APPID 不能为空", groups = {V2.class, V3.class})
    private String appId;
    /**
     * 商户号
     */
    @NotBlank(message = "商户号 不能为空", groups = {V2.class, V3.class})
    private String mchId;
    /**
     * API 版本
     */
    @NotBlank(message = "API 版本 不能为空", groups = {V2.class, V3.class})
    private String apiVersion;

    // ========== V2 版本的参数 ==========

    /**
     * 商户密钥
     */
    @NotBlank(message = "商户密钥 不能为空", groups = V2.class)
    private String mchKey;
    /**
     * apiclient_cert.p12 证书文件的绝对路径或者以 classpath: 开头的类路径.
     * 对应的字符串
     *
     * 注意，可通过 {@link #main(String[])} 读取
     */
    /// private String keyContent;

    // ========== V3 版本的参数 ==========
    /**
     * apiclient_key.pem 证书文件的绝对路径或者以 classpath: 开头的类路径.
     * 对应的字符串
     * 注意，可通过 {@link #main(String[])} 读取
     */
    @NotBlank(message = "apiclient_key 不能为空", groups = V3.class)
    private String privateKeyContent;
    /**
     * apiclient_cert.pem 证书文件的绝对路径或者以 classpath: 开头的类路径.
     * 对应的字符串
     * <p>
     * 注意，可通过 {@link #main(String[])} 读取
     */
    @NotBlank(message = "apiclient_cert 不能为空", groups = V3.class)
    private String privateCertContent;
    /**
     * apiV3 秘钥值
     */
    @NotBlank(message = "apiV3 秘钥值 不能为空", groups = V3.class)
    private String apiV3Key;


    /**
     * 分组校验 v2版本
     */
    public interface V2 {
    }

    /**
     * 分组校验 v3版本
     */
    public interface V3 {
    }

    /**
     * 验证配置参数是否正确
     * @param validator 校验对象
     */
    @Override
    public void verifyParam(Validator validator) {
        // 手动调用validate进行验证
        Set<ConstraintViolation<PayClientConfig>> validate = validator.validate(this,
                this.getApiVersion().equals(API_VERSION_V2) ? V2.class : V3.class);
        // 断言没有异常
        Assert.isTrue(validate.isEmpty(), validate.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(",")));
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "/Users/yunai/Downloads/wx_pay/apiclient_cert.p12";
        /// String path = "/Users/yunai/Downloads/wx_pay/apiclient_key.pem";
        /// String path = "/Users/yunai/Downloads/wx_pay/apiclient_cert.pem";
        System.out.println(IoUtil.readUtf8(new FileInputStream(path)));
    }


}
