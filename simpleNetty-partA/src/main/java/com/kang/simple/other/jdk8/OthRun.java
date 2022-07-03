package com.kang.simple.other.jdk8;

/**
 * User:
 * Description:
 * Date: 2022-05-18
 * Time: 23:44
 */
public class OthRun {

    public void action(Action action){
        action.run("hello");
    }

    public static void main(String[] args) {
        new OthRun().action((String s)->
                System.out.println(s)
        );

        //alt+enter即可自动切换成Lambda
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("run()");
            }
        });
        new Thread(() -> System.out.println("run()"));
    }
}
