package com.dtf.dashboard;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.MessageFormat;

/**
 * @author dtf
 * @desc
 * @date 2021-01-25 14:41
 */
public class SignDemo {

    private final static String SECRET = "zTa20ys1SzYx607";
    private final static String SIGN_TEMPLATE = "clientid={0}&clientVersion={1}&data={2}&device={3}&method={4}&nonce={5}&osVersion={6}&secret={7}&timestamp={8}&udid={9}";

    public static void main(String[] args) {

        //13460037309
        String clientid = "5263b07f-1f15-4340-9e0a-27a17fca7f37";
        String clientVersion = "3.9.46";

        String data = "{\"id\":119,\"memberId\":\"f722a4a59c8f48a2ad7f1fae741f5ba8\",\"phoneNum\":\"13460037309\",\"prizeNum\":3,\"pwd\":\"YXlzMTIz\"}";
        String device = "ios";

        String method = "drawContent.draw";

        String nonce = "1542067447";

        String osVersion = "14.3";

        String timestamp = "1611542141";

        String udid = "4E24CA70-F793-4A75-9C8A-45828A8963E5";

        String inputData = MessageFormat.format(SIGN_TEMPLATE,clientid,clientVersion,data,device,method,nonce,osVersion,SECRET,timestamp,udid);
        System.out.println("inputData = " + inputData);
        String localSign = DigestUtils.sha1Hex(inputData);
        System.out.println("localSign = " + localSign);

        SignEntity signEntity = new SignEntity(clientid, clientVersion, data, device, method, nonce, osVersion, timestamp, udid,localSign);
        String paramJson = JSON.toJSONString(signEntity);

        System.out.println("paramJson = " + paramJson);


    }

    public static class SignEntity {
        private String clientid;
        private String clientVersion;
        private String data;
        private String device;
        private String method;
        private String nonce;
        private String osVersion;
        private String timestamp;
        private String udid;
        private String signature;


        public SignEntity(String clientid, String clientVersion, String data, String device, String method, String nonce, String osVersion, String timestamp, String udid, String signature) {
            this.clientid = clientid;
            this.clientVersion = clientVersion;
            this.data = data;
            this.device = device;
            this.method = method;
            this.nonce = nonce;
            this.osVersion = osVersion;
            this.timestamp = timestamp;
            this.udid = udid;
            this.signature = signature;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public String getClientVersion() {
            return clientVersion;
        }

        public void setClientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUdid() {
            return udid;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }
    }


}