package com.hspedu.hspliving;

// import io.prometheus.client.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 韩顺平
 * @version 1.0
 * 讲解流式计算的将集合转成map
 */
public class T2 {
    public static void main(String[] args) {

        //先构建一个集合
        // ArrayList<Stu> stus = new ArrayList<>();
        //
        // stus.add(new Stu(100, "jack", "北京", "打篮球"));
        // stus.add(new Stu(200, "mary", "上海", "踢足球"));
        // stus.add(new Stu(300, "smith", "广州", "运动"));
        // stus.add(new Stu(400, "milan", "深圳", "唱歌"));

        //需求
        //将 stus->map k-string - v List<Stu2>
        //Stu(100, "jack","北京", "打篮球") -> 100 -> 两个Stu2【("jack","打篮球"), ("jack-克隆","打篮球")】
        //思路-> 使用循环 -> 使用流式计算的 将 集合转成 map  Collectors.toMap

        //Map<String, List<Stu2>> maps = stus.stream().collect(
        //        Collectors.toMap(k -> {
        //            System.out.println("k-->" + k);
        //            return k.getId().toString();
        //        }, v -> {
        //            System.out.println("v--->" + v);
        //            List<Stu2> stu2List = new ArrayList<>();
        //            stu2List.add(new Stu2(v.getName(), v.getHobby()));
        //            stu2List.add(new Stu2(v.getName() + "-克隆", v.getHobby()));
        //            return stu2List;
        //        }));

        // Map<String, List<Stu2>> maps = stus.stream().collect(
        //         Collectors.toMap(k ->
        //             k.getId().toString()
        //         , v -> {
        //             System.out.println("v--->" + v);
        //             List<Stu2> stu2List = new ArrayList<>();
        //             stu2List.add(new Stu2(v.getName(), v.getHobby()));
        //             stu2List.add(new Stu2(v.getName() + "-克隆", v.getHobby()));
        //             return stu2List;
        //         }));

        // System.out.println("maps->>" + maps);
        //-----------------------------------------------------------------------

        // 自己进行测试:
        //先构建一个集合
        ArrayList<Stu> stus = new ArrayList<>();

        stus.add(new Stu(100, "jack", "北京", "打篮球"));
        stus.add(new Stu(200, "mary", "上海", "踢足球"));
        stus.add(new Stu(300, "smith", "广州", "运动"));
        stus.add(new Stu(400, "milan", "深圳", "唱歌"));

        //需求
        //将 stus->map k-string - v List<Stu2>
        //Stu(100, "jack","北京", "打篮球") -> 100 -> 两个Stu2【("jack","打篮球"), ("jack-克隆","打篮球")】
        //思路-> 使用循环 -> 使用流式计算的 将 集合转成 map  Collectors.toMap

        Map<String,List<Stu2>> maps = stus.stream().collect(Collectors.toMap(k->{
            // 这里的 k 是stus集合遍历后的单个的stu对象
            // System.out.println("k==> " + k);
            // k==> Stu{id=100, name='jack', address='北京', hobby='打篮球'}

            // 这里返回的类型要和 Map<String,List<Stu2>> maps 的第一个类型String对应
            return k.getId().toString();

            // return null;

        },v->{
            // 这里的 v 也是stus集合遍历后的单个的stu对象
            // System.out.println("v==> " + v);
            // v==> Stu{id=100, name='jack', address='北京', hobby='打篮球'}


            ArrayList<Stu2> stu2s = new ArrayList<>();

            // stu2s.add(new Stu2("克莉丝缇娜","叉子"));
            // stu2s.add(new Stu2("冈部伦太郎","科学"));

            // 使用传进来的v 构建 maps 集合 的键值对的值
            stu2s.add(new Stu2(v.getName(),v.getHobby()));
            stu2s.add(new Stu2(v.getName()+"-克隆",v.getHobby()+"-克隆"));

            // 这里返回的类型要和 Map<String,List<Stu2>> maps 的第二个类型List<Stu2>对应
            // 否则报错！！！
            return stu2s;
            // return null;
        }));

        System.out.println("map==> " + maps);


    }
}

class Stu2 {
    private String name;
    private String hobby;

    public Stu2(String name, String hobby) {
        this.name = name;
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Stu2{" +
                "name='" + name + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}

class Stu {
    private Integer id;
    private String name;
    private String address;
    private String hobby;

    public Stu(Integer id, String name, String address, String hobby) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.hobby = hobby;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
