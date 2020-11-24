package com.exc.street.light.resource.dto.electricity;

/**
 * @Author:xujiahao
 * @Description 节点在线信息返回类
 * @Data:Created in 21:38 2018/10/12
 * @Modified By:
 */
public class OnlineStr {
    private String nodeName; //节点名称
    private int isOnline; //节点是否在线
    private String offlineTime; //节点离线时长

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }
}
