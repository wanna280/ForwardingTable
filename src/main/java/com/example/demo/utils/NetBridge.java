package com.example.demo.utils;

public class NetBridge {
    int max = 128;   //转发表的最大表项
    static int num = 0;  //转发表当前的表项数目
    String[][] ForwardingTable = new String[max][2];   //转发表

    /**
     * 打印当前的转发表情况
     */
    public void ShowForwardingTable() {    //打印当前的转发表的情况
        System.out.println("\tMacAddress\t\tInterface");   //打印转发表的属性列
        for (int i = 0; i < max; i++) {   //遍历转发表
            for (int j = 0; j < 2; j++) {
                if (ForwardingTable[i][j] != null) {
                    System.out.print(ForwardingTable[i][j] + "\t");   //打印出来表项
                }

            }
            System.out.println();
            if (ForwardingTable[i][1] == null) break;   //如果大打印到空值，结束打印
        }
        System.out.print("*******************************************************");
        System.out.println("*******************************************************");
    }

    /**
     * 数据帧的源地址是否在转发表中，在的话返回true，否则返回false
     * @param dataframe
     * @return
     */
    public boolean IsSourceMacInTable(DataFrame dataframe) {     //原地址是否在转发表中
        for (int i = 0; i < num; i++) {
            if (dataframe != null) {
                if (dataframe.destinationMac.equals(ForwardingTable[i][0])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 数据帧的目的地址是否在转发表中，在的话返回索引，不在的话返回-1
     * @param dataframe 数据帧
     * @return  if in this ForwardingTable,return the index of destinationMac of this dataframe
     * @return else return -1
     */
    public int IsDestinationMacInTable(DataFrame dataframe) {   //目的地址是否在转发表中
        for (int i = 0; i < num; i++) {
            if (dataframe != null) {
                if (dataframe.destinationMac.equals(ForwardingTable[i][0])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 更新转发表
     * @param dataframe  数据帧
     * @param Interface  接口
     */
    public void UpdateForwardingTable(DataFrame dataframe, String Interface) {   //更新转发表
        ForwardingTable[num][0] = dataframe.sourceMac;   //0位置存放源mac
        ForwardingTable[num][1] = Interface;   //1位置存放对应的接口
        num += 1;   //转发表的表项+1
    }

    /**
     * 接收数据帧
     * @param dataframe 数据帧
     * @param Interface 接口
     */
    public void Receive(DataFrame dataframe, String Interface) {
        //如果数据帧的目的mac地址和源mac地址都不为空，才进行接收，否则，不接收
        if (dataframe.sourceMac != null && dataframe.destinationMac != null) {
            System.out.println("Dataframe has been recieved,its sourceMac is "+dataframe.sourceMac+" and the destinationMac is "+dataframe.destinationMac);
            if (IsDestinationMacInTable(dataframe) == -1) {   //如果数据帧的目的地址不在转发表中，打印相关信息
                System.out.println("The destinationMac of dataframe is not in ForwardingTable,try to send by other interface");
                if (!IsSourceMacInTable(dataframe)) {  //如果转发表当中不存在接收数据帧的源地址，将源地址加入转发表
                    UpdateForwardingTable(dataframe, Interface);  //自学习，进行更新
                }
            } else {     //如果数据真的目的地址在转发表中，打印相关信息
                if (!IsSourceMacInTable(dataframe)) {   //如果转发表当中不存在接收数据帧的源地址，将源地址加入转发表
                    UpdateForwardingTable(dataframe, Interface);  //自学习，进行更新
                }
                System.out.println("It can be find in the ForwardingTable");
                if (ForwardingTable[IsDestinationMacInTable(dataframe)][1] == Interface) {   //如果接收端口为发送端口，丢掉数据包
                    System.out.println("SourceMac = DestinationMac!It will be threw up!");
                } else {
                    System.out.println("It will be send from " + ForwardingTable[IsDestinationMacInTable(dataframe)][1]);
                    System.out.println();
                }
            }
            ShowForwardingTable();
        }
    }

}
