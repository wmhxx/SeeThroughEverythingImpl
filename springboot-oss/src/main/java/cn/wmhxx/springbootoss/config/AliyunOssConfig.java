package cn.wmhxx.springbootoss.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssConfig {

    /**
     * 端点
     */
    private String endpoint;
    /**
     * 访问密钥id
     */
    private String accessKeyId;
    /**
     * 访问密钥秘密
     */
    private String accessKeySecret;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 对象名称
     */
    private String objectName;
}
