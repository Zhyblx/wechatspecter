package com.zhangyibin.wechat.util;


/**
 * 类：QRCodeFrame
 * 作用：用于展示微信的登陆二维码
 */

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class QRCodeFrame extends JFrame {

    private JPanel jPanel = new JPanel();
    private static final String FilePath = "image.png";

    /**
     * 启动应用程序
     *
     * @param args(参数)
     * @author zhangyibin
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    new QRCodeFrame(FilePath);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @SuppressWarnings("serial")
    /**
     * 创建窗口;filePath为图片地址
     *
     * @param filePath
     * @author zhangyibin
     */
    public QRCodeFrame(final String filePath) {
        this.setResizable(false);
        this.setTitle("使用手机微信扫码登录-(洛基版)");//扫码登陆微信
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 295, 315);
        this.jPanel.setBackground(new Color(255, 255, 255));
        this.jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(jPanel);
        this.jPanel.setLayout(null);

        JPanel qrcodePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon(filePath);
                // 图片随窗体大小而变化
                g.drawImage(icon.getImage(), 0, 0, 300, 300, this);
            }
        };
        qrcodePanel.setBounds(0, 0, 295, 295);

        this.jPanel.add(qrcodePanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
}



