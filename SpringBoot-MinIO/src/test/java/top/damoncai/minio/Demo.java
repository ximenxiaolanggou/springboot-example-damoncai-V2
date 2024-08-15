package top.damoncai.minio;


import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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

    //******************************** 存储桶操作 - Start ********************************
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
     * 删除bucket
     * 注意，只有存储桶为空时才能删除成功。
     */
    @Test
    public void removeBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = "test";
        RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(bucketName).build();
        minioClient.removeBucket(removeBucketArgs);
    }

    /**
     * 列出某个存储桶中的所有对象。
     *
     * bucketName: 存储桶名称。
     * prefix: 对象名称的前缀，列出有该前缀的对象
     * recursive: 是否递归查找，如果是false,就模拟文件夹结构查找
     */
    @Test
    public void listObjects() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = "test";
        ListObjectsArgs listObjects = ListObjectsArgs.builder().bucket(bucketName).prefix("").build();
        Iterable<Result<Item>> objs = minioClient.listObjects(listObjects);

        for (Result<Item> obj : objs) {
            Item item = obj.get();
            System.out.println(item.objectName() + " - " +item.size());
        }
    }


    /**
     * 获得指定对象前缀的存储桶策略
     */
    @Test
    public void getBucketPolicy() throws Exception {
        String bucketName = "test";
        GetBucketPolicyArgs getBucketPolicy = GetBucketPolicyArgs.builder().bucket(bucketName).build();
        String bucketPolicy = minioClient.getBucketPolicy(getBucketPolicy);
        System.out.println(bucketPolicy);

    }


    /**
     * 占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";

    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + "test" + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + "test" + "/*\"]}]}";


    /**
     * bucket权限-只读
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + "test" + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + "test" + "/*\"]}]}";


    /**
     * 设置指定对象前缀的存储桶策略
     */
    @Test
    public void setBucketPolicy() throws Exception {
        String bucketName = "test";
        SetBucketPolicyArgs setBucketPolicy = SetBucketPolicyArgs.builder().bucket(bucketName).config(WRITE_ONLY).build();
        this.minioClient.setBucketPolicy(setBucketPolicy);
    }


    /**
     * 以流的形式下载一个对象
     */
    @Test
    public void getObject() throws Exception {
        String bucketName = "test";
        GetObjectArgs getObject = GetObjectArgs.builder().bucket(bucketName).object("a.jpg").build();
        GetObjectResponse gor = this.minioClient.getObject(getObject);
        byte[] b = new byte[1024];
        gor.read(b);
        FileOutputStream fos = new FileOutputStream("D:\\aaa.jpg");
        fos.write(b);
    }


    /**
     * 下载文件
     */
    @Test
    public void download() throws Exception {
        String bucketName = "test";
        DownloadObjectArgs build = DownloadObjectArgs.builder().bucket(bucketName).object("a.jpg").filename("D:\\kkk.jpg").build();
        minioClient.downloadObject(build);
    }

    /**
     * 通过InputStream上传对象。
     */
    @Test
    public void putObj() throws Exception {
        String bucketName = "test";
        File file = new File("D:\\kkk.jpg");
        FileInputStream fis = new FileInputStream(file);
        PutObjectArgs put = PutObjectArgs.builder().bucket(bucketName).object("hello.png").stream(fis, file.length(), -1).build();
        minioClient.putObject(put);
    }


    /**
     * 通过文件上传对象。
     */
    @Test
    public void upload() throws Exception {
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs
                .builder()
                .bucket("test")
                .object("hello/a.jpg")
                .filename("d:\\2.png")
                .build();
        minioClient.uploadObject(uploadObjectArgs);
    }

    /**
     * Presigned操作
     * expiry: 失效时间（以秒为单位），默认是7天，不得大于七天
     */
    @Test
    public void presigned() throws Exception {
        GetPresignedObjectUrlArgs test = GetPresignedObjectUrlArgs.builder().bucket("test").object("a.jpg").method(Method.GET).build();
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(test);
        System.out.println(presignedObjectUrl);
    }

    /**
     * 删除对象
     * @throws Exception
     */
    @Test
    public void removeObj() throws Exception {
        RemoveObjectArgs args = RemoveObjectArgs.builder().bucket("13236128814").object("ll/位置度2.xlsx").build();
        minioClient.removeObject(args);
    }


}
