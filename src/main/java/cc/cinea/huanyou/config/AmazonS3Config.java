package cc.cinea.huanyou.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * 备注：其实用的是阿里云，因为国内用不了aws s3
 *
 * @author cinea
 */
@Configuration
public class AmazonS3Config {
    @Value("${app.s3.accessKey}")
    String accessKey;

    @Value("${app.s3.accessSecret}")
    String accessSecret;

    @Value("${app.s3.endpoint}")
    String endpoint;

    @Value("${app.s3.region}")
    String region;

    @Bean
    @SneakyThrows
    AmazonS3 amazonS3() {
        URL endpointUrl = new URL(endpoint);
        String protocol = endpointUrl.getProtocol();
        int port = endpointUrl.getPort() == -1 ? endpointUrl.getDefaultPort() : endpointUrl.getPort();

        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setSignerOverride("S3SignerType");
        configuration.setProtocol(Protocol.valueOf(protocol.toUpperCase()));

        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, accessSecret));

        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentials)
                .withClientConfiguration(configuration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }

}
