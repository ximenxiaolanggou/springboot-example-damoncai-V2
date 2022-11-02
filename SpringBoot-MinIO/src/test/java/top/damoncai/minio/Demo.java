package top.damoncai.minio;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Demo {

    MinioClient minioClient;



    @Before
    public void before() {
        minioClient = MinioClient.builder()
                .endpoint("http://150.158.78.149:9000")
                .credentials("admin", "damoncai")
                .build();
    }

    /**
     * 判断bucket是否存在
     */
    @Test
    public void existBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = "test";
        BucketExistsArgs bucket = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exist = minioClient.bucketExists(bucket);
        System.out.println(bucketName + ":" + exist);
    }

    /**
     * 创建bucket
     */
    @Test
    public void createBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = "test";
        BucketExistsArgs bucket = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean exist = minioClient.bucketExists(bucket);
        if(!exist)
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * bucket 列表
     */
    @Test
    public void bucketList() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.name() + " - " + bucket.creationDate());
        }
    }

    /**
     * 上传
     */
    @Test
    public void upload() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    }

}
