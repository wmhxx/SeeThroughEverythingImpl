package cn.wmhxx.springbootoss.controller;

import cn.wmhxx.springbootoss.config.AliyunOssConfig;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

/**
 * 文件控制器
 *
 * @author wmhxx.cn@outlook.com
 * @date 2022-05-24
 */
@Slf4j
@RequestMapping("/oss")
@RestController
public class FileController {

    @Resource
    private AliyunOssConfig aliyunOssConfig;

    /**
     * 上传 文本
     *
     * @return {@link Boolean}
     */
    @PostMapping("/uploadFile")
    public Boolean uploadFile() {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = aliyunOssConfig.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = aliyunOssConfig.getAccessKeyId();
        String accessKeySecret = aliyunOssConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = aliyunOssConfig.getBucketName();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = aliyunOssConfig.getObjectName();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 填写字符串。
            String content = "Hello OSS";

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

            // 上传字符串。
            ossClient.putObject(putObjectRequest);
            log.info("文件上传成功");
            return true;
        } catch (OSSException oe) {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message:" + oe.getErrorMessage());
            log.info("Error Code:" + oe.getErrorCode());
            log.info("Request ID:" + oe.getRequestId());
            log.info("Host ID:" + oe.getHostId());
            log.info("文件上传失败 - OSSException:{}", oe.getMessage());
            return false;
        } catch (ClientException ce) {
            log.info("Caught an ClientException, which means the client encountered a "
                    + "serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message:{}", ce.getMessage());
            log.info("文件上传失败 - ClientException:{}", ce.getMessage());
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
