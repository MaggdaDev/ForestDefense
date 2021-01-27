/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.network.server.ServerSocketHandler;

/**
 *
 * @author DavidPrivat
 */
public class UdpReceiver extends Thread {

    private boolean useUDP = true;
    private DatagramSocket udpSocket;
    private DatagramPacket udpPacket;
    private byte[] udpByteBuffer;

    public UdpReceiver() {
        try {
            udpSocket = new DatagramSocket(Server.UDP_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        udpByteBuffer = new byte[ServerSocketHandler.byteBufferLength];
        udpPacket = new DatagramPacket(udpByteBuffer, ServerSocketHandler.byteBufferLength);

    }

    @Override
    public void run() {
        while (true) {
            try {
                udpSocket.receive(udpPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = new String(udpPacket.getData());
            NetworkManager.getInstance().getCommandHandler().onMessage(message);
        }
    }
}
