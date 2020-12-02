package com.example.demo;

import com.example.demo.utils.NetBridge;
import com.example.demo.utils.DataFrame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DemoApplication {

    /**
     * 从文件1读取两行代表目的地址和源地址，从文件2读取两行代表目的地址和源地址
     * 文件1和文件2分别木你两个接口Interface1和Interface2接收到的数据帧
     * @param args
     */
    public static void main(String[] args) {

        FileReader reader1 = null, reader2 = null;   //两个文件阅读指针
        BufferedReader bufferedReader1 = null, bufferedReader2 = null;   //创建两个缓冲区指针
        try {
            reader1 = new FileReader("interface1.txt");  //文件阅读指针reader1读取源
            bufferedReader1 = new BufferedReader(reader1);  //读文件1

            reader2 = new FileReader("interface2.txt");   //文件阅读指针reader2读取目的
            bufferedReader2 = new BufferedReader(reader2);  //读文件2


            NetBridge netBridge = new NetBridge();   //创建网桥对象
            DataFrame dataframe = new DataFrame();   //创建数据帧对象
            while (bufferedReader1 != null || bufferedReader2 != null) {
                if (bufferedReader1 != null) {
                    try {
                        dataframe.destinationMac = bufferedReader1.readLine(); //从读取文件1读取一行代表目的地址
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        dataframe.sourceMac = bufferedReader1.readLine();  //从文件1读一行代表源地址
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (dataframe.destinationMac != null && dataframe.sourceMac != null) { //如果源地址和目的地址都不为空
                        netBridge.Receive(dataframe, "Interface0");  //网关（路由器）接收这个数据帧
                    } else {
                        break;
                    }
                } else {
                    try {   //如果为这个读文件指针为空
                        reader1.close();  //关闭这个读文件指针
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bufferedReader2 != null) {
                    try {
                        dataframe.destinationMac = bufferedReader2.readLine(); //从读取文件2读取一行代表目的地址
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        dataframe.sourceMac = bufferedReader2.readLine(); //从读取文件2读取一行代表目的地址
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (dataframe.sourceMac != null && dataframe.destinationMac != null) { //如果源地址和目的地址都不为空
                        netBridge.Receive(dataframe, "Interface1"); //网关（路由器）接收这个数据帧
                    } else {
                        break;
                    }
                } else {  //如果为这个读文件指针为空
                    try {
                        reader2.close();  //关闭这个读文件指针
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
