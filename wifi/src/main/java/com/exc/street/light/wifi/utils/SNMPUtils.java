package com.exc.street.light.wifi.utils;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 演示： GET单个OID值
 * <p>
 * blog http://www.micmiu.com
 *
 * @author xiezhipeng
 */
public class SNMPUtils {

    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 2 * 500L;
    public static final int DEFAULT_RETRY = 0;

    /**
     * 创建对象communityTarget，用于返回target
     *
     * @param community
     * @return CommunityTarget
     */
    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        // milliseconds
        target.setTimeout(DEFAULT_TIMEOUT);
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 根据OID，获取单条消息
     *
     * @param ip
     * @param community
     * @param oid
     * @return
     */
    public static Object snmpGet(String ip, String community, String oid) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        DefaultUdpTransportMapping transport = null;
        // 初始化多线程消息转发类
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(messageDispatcher, transport);
            snmp.listen();
            System.out.println("-------> 发送PDU <-------");
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();
            if (response == null) {
                System.out.println("response is null, request time out");
                return null;
            } else {
                System.out.println("response pdu size is " + response.size());
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                    return vb.getVariable();
                }
            }
            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    // 由于该监听进程是守护进程，最后应调用close()方法来释放该进程
                    transport.close();
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
        return null;
    }

    /**
     * 根据OID列表，一次获取多条OID数据，并且以List形式返回
     *
     * @param ip
     * @param community
     * @param oidList
     */
    public static void snmpGetList(String ip, String community, List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            System.out.println("-------> 发送PDU <-------");
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();
            if (response == null) {
                System.out.println("response is null, request time out");
            } else {
                System.out.println("response pdu size is " + response.size());
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                }
            }
            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
    }

    /**
     * 根据OID列表，采用异步方式一次获取多条OID数据，并且以List形式返回
     *
     * @param ip
     * @param community
     * @param oidList
     */
    public static void snmpAsynGetList(String ip, String community, List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            System.out.println("-------> 发送PDU <-------");
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();
            /*异步获取*/
            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    PDU request = event.getRequest();
                    System.out.println("[request]:" + request);
                    if (response == null) {
                        System.out.println("[ERROR]: response is null");
                    } else if (response.getErrorStatus() != 0) {
                        System.out.println("[ERROR]: response status"
                                + response.getErrorStatus() + " Text:"
                                + response.getErrorStatusText());
                    } else {
                        System.out.println("Received response Success!");
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
                            System.out.println(vb.getOid() + " = "
                                    + vb.getVariable());
                        }
                        System.out.println("SNMP Asyn GetList OID finished. ");
                        latch.countDown();
                    }
                }
            };
            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            System.out.println("asyn send pdu wait for response...");
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);
            snmp.close();
            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
    }

    /**
     * 根据targetOID，获取树形数据
     *
     * @param ip
     * @param community
     * @param targetOid
     * @return
     */
    public static List<VariableBinding> snmpWalk(String ip, String community, String targetOid) {
        List<VariableBinding> list = new ArrayList<>();
        CommunityTarget target = createDefault(ip, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        // 初始化多线程消息转发类
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(messageDispatcher, transport);
            snmp.listen();
            PDU pdu = new PDU();
            OID targetOID = new OID(targetOid);
            pdu.add(new VariableBinding(targetOID));
            boolean finished = false;
            System.out.println("----> demo start <----");
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvent = snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();
                if (null == response) {
                    System.out.println("responsePDU == null");
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                // check finish
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
                    System.out.println("==== walk each vlaue :");
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                    list.add(vb);
                    // Set up the variable binding for the next entry.
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    System.out.println("SNMP walk OID has finished.");
                    transport.close();
                    snmp.close();
                }
            }
            System.out.println("----> demo end <----");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP walk Exception: " + e);
        } finally {
            if (snmp != null) {
                try {
                    transport.close();
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return null;
    }

    private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            System.out.println("[true] responsePDU.getErrorStatus() != 0 ");
            System.out.println(pdu.getErrorStatusText());
            finished = true;
        } else if (vb.getOid() == null) {
            System.out.println("[true] vb.getOid() == null");
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            System.out.println("[true] vb.getOid().size() < targetOID.size()");
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            System.out.println("[true] targetOID.leftMostCompare() != 0");
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            System.out
                    .println("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            System.out.println("[true] Variable received is not "
                    + "lexicographic successor of requested " + "one:");
            System.out.println(vb.toString() + " <= " + targetOID);
            finished = true;
        }
        return finished;

    }

    /**
     * 根据targetOID，异步获取树形数据
     *
     * @param ip
     * @param community
     * @param oid
     * @return
     */
    public static List<VariableBinding> snmpAsynWalk(String ip, String community, String oid) {
        List<VariableBinding> list = new ArrayList<>();
        final CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        // 初始化多线程消息转发类
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        try {
            System.out.println("----> demo start <----");
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(messageDispatcher, transport);
            snmp.listen();
            final PDU pdu = new PDU();
            final OID targetOID = new OID(oid);
            final CountDownLatch latch = new CountDownLatch(1);
            pdu.add(new VariableBinding(targetOID));
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    try {
                        PDU response = event.getResponse();
                        if (response == null) {
                            System.out.println("[ERROR]: response is null");
                        } else if (response.getErrorStatus() != 0) {
                            System.out.println("[ERROR]: response status" + response.getErrorStatus() + " Text:" + response.getErrorStatusText());
                        } else {
                            System.out.println("Received Walk response value :");
                            VariableBinding vb = response.get(0);
                            boolean finished = checkWalkFinished(targetOID, pdu, vb);
                            if (!finished) {
                                System.out.println(vb.getOid() + " = " + vb.getVariable());
                                list.add(vb);
                                pdu.setRequestID(new Integer32(0));
                                pdu.set(0, vb);
                                ((Snmp) event.getSource()).getNext(pdu, target, null, this);
                            } else {
                                System.out.println("SNMP Asyn walk OID value success !");
                                latch.countDown();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }
                }
            };
            snmp.getNext(pdu, target, null, listener);
            System.out.println("pdu 已发送,等到异步处理结果...");
            boolean wait = latch.await(3, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);
            snmp.close();
            System.out.println("----> demo end <----");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Asyn Walk Exception:" + e);
        }
        return list;
    }

    /**
     * 根据OID和指定string来设置设备的数据
     *
     * @param ip
     * @param community
     * @param oid
     * @param val
     * @throws IOException
     */
    public static void setPDU(String ip, String community, String oid, String val) throws IOException {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid), new OctetString(val)));
        pdu.setType(PDU.SET);
        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        snmp.listen();
        System.out.println("-------> 发送PDU <-------");
        snmp.send(pdu, target);
        snmp.close();
    }

    public static void main(String[] args) {

        List<VariableBinding> list = snmpWalk("192.168.0.30", "ruijie", "1.3.6.1.4.1.4881.1.1.10.2.36.1.1.1.0");
        for (VariableBinding binding : list) {
            String s = binding.getOid().toString();
            System.out.println(s);
            System.out.println(binding.getVariable().toString());
        }
    }
}
