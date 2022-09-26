package asia.dreamdropsakura.reggie.util;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送工具类
 *
 * @author 童话的爱
 * @since 2022-9-25
 */
@Slf4j
public class SMSUtils {

	/**
	 * 发送短信
	 *
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 * @throws Exception
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param) throws Exception {
		signName = "阿里云短信测试";
		templateCode = "SMS_154950909";
		phoneNumbers = "15670600000";

		// 利用工具类生成验证码
		Integer verifyCode = ValidateCodeUtils.generateValidateCode(4);

		// todo 这里的akId 与akSe 需要设置，先在阿里云中开通短信服务，再在阿里云中进行进行在线API测试
		//  https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms?spm=5176.25163407.quickstart-index-d6f48_f4003_0.d_65e2c_8da05_2.7316bb6eKJH3b8&params={%22SignName%22:%22%E9%98%BF%E9%87%8C%E4%BA%91%E7%9F%AD%E4%BF%A1%E6%B5%8B%E8%AF%95%22,%22TemplateCode%22:%22SMS_154950909%22,%22PhoneNumbers%22:%2215670000000%22,%22TemplateParam%22:%22{\%22code\%22:\%221234\%22}%22}&lang=JAVA&sdkStyle=old
		com.aliyun.dysmsapi20170525.Client client = SMSUtils.createClient("akId", "akSe");
		com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
				.setSignName(signName)
				.setTemplateCode(templateCode)
				.setPhoneNumbers(phoneNumbers)
				.setTemplateParam("{\"code\":" + verifyCode + "}");
		com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
		try {
			// 复制代码运行请自行打印 API 的返回值
			SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
			log.info(sendSmsResponse.getBody().getMessage());
			log.info("Sms has been send, code is " + verifyCode);

		} catch (TeaException error) {
			// 如有需要，请打印 error
			com.aliyun.teautil.Common.assertAsString(error.message);
		} catch (Exception _error) {
			TeaException error = new TeaException(_error.getMessage(), _error);
			// 如有需要，请打印 error
			com.aliyun.teautil.Common.assertAsString(error.message);
		}
	}

	/**
	 * 使用AK&SK初始化账号Client
	 *
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @return Client
	 * @throws Exception
	 */
	public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
				// 您的 AccessKey ID
				.setAccessKeyId(accessKeyId)
				// 您的 AccessKey Secret
				.setAccessKeySecret(accessKeySecret);
		// 访问的域名
		config.endpoint = "dysmsapi.aliyuncs.com";
		return new com.aliyun.dysmsapi20170525.Client(config);
	}
}
