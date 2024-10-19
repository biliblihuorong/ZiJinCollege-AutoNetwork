package moe.ning.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueryString {

    private String wlanuserip;
    private String wlanacname;
    private String ssid;
    private String nasip;
    private String snmpagentip;
    private String mac;
    private String t;
    private String url;
    private String apmac;
    private String nasid;
    private String vid;
    private String port;
    private String nasportid;
    private String operatorPwd;
    private String operatorUserId;
    private String validcode;
    private boolean passwordEncrypt;
}
