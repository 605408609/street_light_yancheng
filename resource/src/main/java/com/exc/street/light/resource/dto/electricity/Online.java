package com.exc.street.light.resource.dto.electricity;

import java.util.Date;

/**
 * @Author:xujiahao
 * @Description 在线状态集合
 * @Data:Created in 9:38 2018/2/2
 * @Modified By:
 */
public class Online {
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点是否在线 0-在线 1离线
     */
    private int isOnline;
    /**
     * 节点离线时长
     */
    private Date lostTime;

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

    public Date getLostTime() {
        return lostTime;
    }

    public void setLostTime(Date lostTime) {
        this.lostTime = lostTime;
    }

    @Override
    public String toString() {
        return "Online{" +
                "nodeName='" + nodeName + '\'' +
                ", isOnline=" + isOnline +
                ", lostTime=" + lostTime +
                '}';
    }
}
