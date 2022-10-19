package com.dtf;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import zipkin.server.internal.EnableZipkinServer;

/**
 * @author dtf
 * @desc zipKin高可用
 * @date 2022-10-19 15:05
 */
@EnableZipkinServer
@SpringCloudApplication
public class ZipkinApplication {
}
